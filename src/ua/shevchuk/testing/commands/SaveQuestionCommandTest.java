package ua.shevchuk.testing.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.shevchuk.controller.commands.SaveQuestionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Answer;
import ua.shevchuk.logic.Question;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.User;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for SaveQuestionCommand class
 */
public class SaveQuestionCommandTest {

	private SaveQuestionCommand command;
	private DataSource dataSource;
	private User user;
	private Subject subject;
	private TestingRequestWrapper request;			
	private ua.shevchuk.logic.Test test;
	
	@Before
	public void init() {
		command = new SaveQuestionCommand();
		dataSource = new TestingDataSource();
		DaoFactory.setDataSource(dataSource);
		try {
			user = DaoFactory.getUserDao().getWithMaxLogin();
			subject = DaoFactory.getSubjectDao().getAny();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		request = new TestingRequestWrapper();			
		request.setSessionAttribute("user", user);
		request.setSessionAttribute("subject", subject);
		request.setParameter("questionNumber", "1");
	}

	@Test
	public void executeCreateQuestionTest() {
		test = new ua.shevchuk.logic.Test(0, 0, subject, user, new ArrayList<>());
		request.setSessionAttribute("test", test);
		request.setParameter("size", "2");
		request.setParameter("text", "Question");
		request.setParameter("text1", "Answer1");
		request.setParameter("correct1", "true");
		String path = command.execute(request);
		Assert.assertEquals("/create.jsp", path);
		Assert.assertTrue(test.getId() == 0);
		Assert.assertEquals(request.getAttribute("size"), 2);
		Assert.assertEquals(request.getAttribute("questionNumber"), 2);
	}

	@Test
	public void executeCreateLastQuestionTest() {
		test = new ua.shevchuk.logic.Test(0, 0, subject, user, new ArrayList<>());
		request.setSessionAttribute("test", test);
		request.setParameter("size", "1");
		request.setParameter("text", "Question");
		request.setParameter("text1", "Answer1");
		request.setParameter("correct1", "true");
		String path = command.execute(request);
		Assert.assertEquals("/tests.jsp", path);
		Assert.assertTrue(test.getId() != 0);
		Assert.assertNotNull(request.getSessionAttribute("tests"));
	}

	@Test
	public void executeAnswerQuestionTest() {
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer(1, "Answer", true, false));
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(1, "Question1", null, null, answers));
		questions.add(new Question(2, "Question2", null, null, null));
		test = new ua.shevchuk.logic.Test(-1, 0, subject, user, questions);
		request.setSessionAttribute("test", test);
		request.setParameter("testId", Integer.toString(test.getId()));
		request.setParameter("correct1", "true");
		String path = command.execute(request);
		Assert.assertEquals("/question.jsp", path);
		Assert.assertFalse(test.isPassed());
		Assert.assertNotNull(request.getAttribute("question"));
		Assert.assertEquals(request.getAttribute("testId"), test.getId());
		Assert.assertEquals(request.getAttribute("questionNumber"), 2);
	}

	@Test
	public void executeAnswerLastQuestionTest() {
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer(0, "Answer", true, false));
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(0, "Question1", null, null, answers));
		test = new ua.shevchuk.logic.Test(0, 0, subject, user, questions);
		try {
			DaoFactory.getTestDao().create(test);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setSessionAttribute("test", test);
		request.setParameter("testId", Integer.toString(test.getId()));
		request.setParameter("correct1", "true");
		String path = command.execute(request);
		Assert.assertEquals("/result.jsp", path);
		Assert.assertTrue(test.isPassed());
		Assert.assertNotNull(request.getAttribute("question"));
		Assert.assertEquals(request.getAttribute("testId"), test.getId());
		Assert.assertEquals(request.getAttribute("questionNumber"), 1);
	}

	@Test
	public void executeViewQuestionTest() {
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer(1, "Answer", true, false));
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(1, "Question1", null, null, answers));
		questions.add(new Question(2, "Question2", null, null, null));
		test = new ua.shevchuk.logic.Test(-1, 0, subject, user, questions);
		test.setPassed(true);
		request.setSessionAttribute("test", test);
		request.setParameter("testId", Integer.toString(test.getId()));
		String path = command.execute(request);
		Assert.assertEquals("/result.jsp", path);
		Assert.assertNotNull(request.getAttribute("question"));
		Assert.assertEquals(request.getAttribute("testId"), test.getId());
		Assert.assertEquals(request.getAttribute("questionNumber"), 2);
	}

	@Test
	public void executeViewLastQuestionTest() {
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer(1, "Answer", true, false));
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(1, "Question1", null, null, answers));
		test = new ua.shevchuk.logic.Test(-1, 0, subject, user, questions);
		test.setPassed(true);
		request.setSessionAttribute("test", test);
		request.setParameter("testId", Integer.toString(test.getId()));
		String path = command.execute(request);
		Assert.assertEquals("/tests.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("tests"));
	}

	@After
	public void destroy() {
		if (test.getId() != 0) {
			try {
				DaoFactory.getTestDao().delete(test.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
