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

/**
 * This class provides access to the table TESTS 
 * and the linked tables QUESTIONS, ANSWERS and RESULTS in the database.
 */
public class TestDao extends AbstractDao {

	private final String GET_TESTS_ON_SUBJECT;
	private final String GET_TEST; 
	private final String CREATE_TEST;
	private final String GET_BY_NUMBER;
	private final String GET_MAX_NUMBER;
	private final String CREATE_QUESTION;
	private final String GET_CREATED_QUESTION;
	private final String CREATE_ANSWER;
	private final String GET_CREATED_ANSWER;
	private final String SAVE_RESULT;
	private final String DELETE_TEST;
	
	/**
	 * @param resourceBundle a resource bundle with SQL queries
	 * @param dataSource a data source
	 */
	public TestDao(ResourceBundle resourceBundle, DataSource dataSource) {
		super(resourceBundle, dataSource);
		
		GET_TESTS_ON_SUBJECT = getResource("TEST.GET_LIST_BY_SUBJECT_WITH_RESULTS");
		GET_TEST = getResource("TEST.GET_WITH_RESULTS");
		CREATE_TEST = getResource("TEST.CREATE");
		GET_BY_NUMBER = getResource("TEST.GET_BY_NUMBER");
		GET_MAX_NUMBER = getResource("TEST.GET_MAX_NUMBER");
		CREATE_QUESTION = getResource("QUESTION.CREATE");
		GET_CREATED_QUESTION = getResource("QUESTION.GET_LAST_BY_TEST");
		CREATE_ANSWER = getResource("ANSWER.CREATE");
		GET_CREATED_ANSWER = getResource("ANSWER.GET_LAST_BY_QUESTION");
		SAVE_RESULT = getResource("RESULT.SAVE");
		DELETE_TEST = getResource("TEST.DELETE");
	}

	/**
	 * Gets a list of tests for a given subject.
	 * @param currentUser an User object representing the user who passes the tests
	 * @param subject a Subject object representing the given subject for testing
	 * @return a List view of the tests for a given subject
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public List<Test> getListBySubject(User currentUser, Subject subject) throws SQLException {
		try (Connection connection = getConnection()) {
			List<Test> list = new ArrayList<>();
	    	Map<Integer, User> userMap = new HashMap<>();
			PreparedStatement statement = connection.prepareStatement(GET_TESTS_ON_SUBJECT);
			statement.setInt(1, currentUser.getId());
			statement.setInt(2, subject.getId());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int userId = resultSet.getInt(3);
				User user = userMap.get(userId);
				if (user == null) {
					user = new User(userId, resultSet.getString(4), null, true);
					userMap.put(userId, user);
				}
				Test test = new Test(resultSet.getInt(1), resultSet.getInt(2), subject, user, null);
				test.setPassed(resultSet.getInt(5) > 0);
				list.add(test);
			}
			resultSet.close();
			statement.close();
			return list;
		}
	}

	/**
	 * Gets a test by its identifier.
	 * @param currentUser an User object representing the user who passes the tests
	 * @param subject a Subject object representing the given subject for testing
	 * @param id a test identifier
	 * @return a Test object
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public Test get(User currentUser, Subject subject, int id) throws SQLException {
		try (Connection connection = getConnection()) {
			Test test = null;
			PreparedStatement statement = connection.prepareStatement(GET_TEST);
			statement.setInt(1, currentUser.getId());
			statement.setInt(2, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User(resultSet.getInt(1), resultSet.getString(3), null, true);
				List<Question> questions = new ArrayList<>();
				test = new Test(id, resultSet.getInt(2), subject, user, questions);
				int questionId = 0;
				List<Answer> answers = null;
				do {
					int newQuestionId = resultSet.getInt(4);
					if (questionId != newQuestionId) {
						questionId = newQuestionId;
						answers = new ArrayList<>();
						questions.add(new Question(questionId, resultSet.getString(5), 
								resultSet.getString(6), resultSet.getString(7), answers));
					}
					boolean result = (resultSet.getInt(11) != 0);
					answers.add(new Answer(resultSet.getInt(8), resultSet.getString(9), 
							resultSet.getBoolean(10), result));
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

	/**
	 * Adds a new test.
	 * @param test a Test object
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public void create(Test test) throws SQLException {
		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);
			
			int userId = test.getUser().getId();
			int subjectId = test.getSubject().getId();

			int testId = 0;
			int number = 0;
			do {
				number = getMaxNumber(connection, subjectId) + 1;

				PreparedStatement statement = connection.prepareStatement(CREATE_TEST);
				statement.setInt(1, subjectId);
				statement.setInt(2, number);
				statement.setInt(3, userId);
				SQLException exception = null;
				try {
					statement.execute();
				} catch (SQLException e) {
					exception = e;
				}
				statement.close();

				testId = getIdByNumber(connection, subjectId, number);
				if (exception != null) {
					if (testId == 0) {
						throw exception; 
					} else {
						testId = 0;
					}
				}
			} while (testId == 0);
			test.setId(testId);
			test.setNumber(number);

			for (Question question : test.getQuestions()) {
				createQuestion(connection, question, testId);
			}

			connection.commit();
		}
	}
	
	private int getMaxNumber(Connection connection, int subjectId) throws SQLException {
		int number = 0;
		PreparedStatement statement = connection.prepareStatement(GET_MAX_NUMBER);
		statement.setInt(1, subjectId);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			number = resultSet.getInt(1);
		}
		resultSet.close();
		statement.close();	
		return number;
	}
	
	private int getIdByNumber(Connection connection, int subjectId, int number) throws SQLException {
		int testId = 0;
		PreparedStatement statement = connection.prepareStatement(GET_BY_NUMBER);
		statement.setInt(1, subjectId);
		statement.setInt(2, number);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			testId = resultSet.getInt(1);
		}
		resultSet.close();
		statement.close();	
		return testId;
	}

	private void createQuestion(Connection connection, Question question, int testId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_QUESTION);
		statement.setInt(1, testId);
		statement.setString(2, question.getText());
		statement.setString(3, question.getCode());
		statement.setString(4, question.getComment());
		statement.execute();
		statement.close();
		
		int questionId = getCreatedQuestionId(connection, testId);
		question.setId(questionId);
		
		for (Answer answer : question.getAnswers()) {
			createAnswer(connection, answer, questionId);
		}
	}

	private int getCreatedQuestionId(Connection connection, int testId) throws SQLException {
		int questionId = 0;
		PreparedStatement statement = connection.prepareStatement(GET_CREATED_QUESTION);
		statement.setInt(1, testId);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		questionId = resultSet.getInt(1);
		resultSet.close();
		statement.close();
		return questionId;	
	}
	
	private void createAnswer(Connection connection, Answer answer, int questionId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(CREATE_ANSWER);
		statement.setInt(1, questionId);
		statement.setString(2, answer.getText());
		statement.setBoolean(3, answer.isCorrect());
		statement.execute();
		statement.close();
	
		answer.setId(getCreatedAnswerId(connection, questionId));
	}

	private int getCreatedAnswerId(Connection connection, int questionId) throws SQLException {
		int answerId = 0;
		PreparedStatement statement = connection.prepareStatement(GET_CREATED_ANSWER);
		statement.setInt(1, questionId);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		answerId = resultSet.getInt(1);
		resultSet.close();
		statement.close();
		return answerId;	
	}

	/**
	 * Saves results of a passed test.
	 * @param currentUser an User object representing the user who passes the tests
	 * @param test a Test object representing the passed test
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public void saveResults(User currentUser, Test test) throws SQLException {
		try (Connection connection = getConnection()) {
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
	
	/**
	 * Deletes a test by its identifier.
	 * @param id a test identifier
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public void delete(int id) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(DELETE_TEST);
			statement.setInt(1, id);
			statement.execute();
			statement.close();
		}
	}

}
