package ua.shevchuk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import ua.shevchuk.logic.Answer;
import ua.shevchuk.logic.Question;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.Test;
import ua.shevchuk.logic.User;

public class TestDao {

	private final String GET_TESTS_ON_SUBJECT;
	private final String GET_TEST; 
	private final String CREATE_TEST;
	private final String GET_CREATED_TEST;
	private final String CREATE_QUESTION;
	private final String GET_CREATED_QUESTION;
	private final String CREATE_ANSWER;
	private final String GET_CREATED_ANSWER;
	private final String SAVE_RESULT;

	private DataSource dataSource;
	
	public TestDao(ResourceBundle resourceBundle, DataSource dataSource) {
		GET_TESTS_ON_SUBJECT = resourceBundle.getString("TEST.GET_LIST_BY_SUBJECT_WITH_RESULTS");
		GET_TEST = resourceBundle.getString("TEST.GET_WITH_RESULTS");
		CREATE_TEST = resourceBundle.getString("TEST.CREATE");
		GET_CREATED_TEST = resourceBundle.getString("TEST.GET_LAST_BY_USER");
		CREATE_QUESTION = resourceBundle.getString("QUESTION.CREATE");
		GET_CREATED_QUESTION = resourceBundle.getString("QUESTION.GET_LAST_BY_TEST");
		CREATE_ANSWER = resourceBundle.getString("ANSWER.CREATE");
		GET_CREATED_ANSWER = resourceBundle.getString("ANSWER.GET_LAST_BY_QUESTION");
		SAVE_RESULT = resourceBundle.getString("RESULT.SAVE");
		
		this.dataSource = dataSource;
	}

	public List<Test> getListBySubject(User currentUser, Subject subject) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			List<Test> list = new ArrayList<>();
	    	Map<Integer, User> userMap = new HashMap<>();
			PreparedStatement statement = connection.prepareStatement(GET_TESTS_ON_SUBJECT);
			statement.setInt(1, currentUser.getId());
			statement.setInt(2, subject.getId());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int userId = resultSet.getInt(2);
				User user = userMap.get(userId);
				if (user == null) {
					user = new User(userId, resultSet.getString(3), null, true);
					userMap.put(userId, user);
				}
				Test test = new Test(resultSet.getInt(1), subject, user, null);
				test.setPassed(resultSet.getInt(4) > 0);
				list.add(test);
			}
			resultSet.close();
			statement.close();
			return list;
		}
	}

	public Test get(User currentUser, Subject subject, int id) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			Test test = null;
			PreparedStatement statement = connection.prepareStatement(GET_TEST);
			statement.setInt(1, currentUser.getId());
			statement.setInt(2, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getInt(1), resultSet.getString(2), null, true);
				List<Question> questions = new ArrayList<>();
				test = new Test(id, subject, user, questions);
				int questionId = 0;
				List<Answer> answers = null;
				do {
					int newQuestionId = resultSet.getInt(3);
					if (questionId != newQuestionId) {
						questionId = newQuestionId;
						answers = new ArrayList<>();
						questions.add(new Question(questionId, resultSet.getString(4), 
								resultSet.getString(5), resultSet.getString(6), answers));
					}
					boolean result = (resultSet.getInt(10) != 0);
					answers.add(new Answer(resultSet.getInt(7), resultSet.getString(8), 
							resultSet.getBoolean(9), result));
					if (result == true) {
						test.setPassed(true);
					}
				} while (resultSet.next());
			}
			resultSet.close();
			statement.close();
			return test;
		}
	}

	public void create(Test test) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);
			
			int userId = test.getUser().getId();
			PreparedStatement statement = connection.prepareStatement(CREATE_TEST);
			statement.setInt(1, test.getSubject().getId());
			statement.setInt(2, userId);
			statement.execute();
			statement.close();
	
			int testId = 0;
			statement = connection.prepareStatement(GET_CREATED_TEST);
			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			testId = resultSet.getInt(1);
			test.setId(testId);
			resultSet.close();
			statement.close();
			
			for (Question question : test.getQuestions()) {
				createQuestion(connection, question, testId);
			}
			
			connection.commit();
		}
	}
	
	private void createQuestion(Connection connection, Question question, int testId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_QUESTION);
		statement.setInt(1, testId);
		statement.setString(2, question.getText());
		statement.setString(3, question.getCode());
		statement.setString(4, question.getComment());
		statement.execute();
		statement.close();
		
		int questionId = 0;
		statement = connection.prepareStatement(GET_CREATED_QUESTION);
		statement.setInt(1, testId);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		questionId = resultSet.getInt(1);
		question.setId(questionId);
		resultSet.close();
		statement.close();	
		
		for (Answer answer : question.getAnswers()) {
			createAnswer(connection, answer, questionId);
		}
	}

	private void createAnswer(Connection connection, Answer answer, int questionId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_ANSWER);
		statement.setInt(1, questionId);
		statement.setString(2, answer.getText());
		statement.setBoolean(3, answer.isCorrect());
		statement.execute();
		statement.close();
	
		statement = connection.prepareStatement(GET_CREATED_ANSWER);
		statement.setInt(1, questionId);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		answer.setId(resultSet.getInt(1));
		resultSet.close();
		statement.close();	
	}

	public void saveResults(User currentUser, Test test) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);
			
			int userId = currentUser.getId();
			for (Question question : test.getQuestions()) {
				for (Answer answer : question.getAnswers()) {
					if (answer.getResult() == true) {
						PreparedStatement statement = connection.prepareStatement(SAVE_RESULT);
						statement.setInt(1, answer.getId());
						statement.setInt(2, userId);
						statement.execute();
						statement.close();
					}
				}
			}
			
			connection.commit();
		}
	}
	
}
