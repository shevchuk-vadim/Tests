package ua.shevchuk.testing.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.shevchuk.controller.commands.StartTestingCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Answer;
import ua.shevchuk.logic.Question;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.User;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for StartTestingCommand class
 */
public class StartTestingCommandTest {

	private StartTestingCommand command;
	private DataSource dataSource;
	private User user;
	private Subject subject;
	private TestingRequestWrapper request;			
	private ua.shevchuk.logic.Test test;
	
	@Before
	public void init() {
		command = new StartTestingCommand();
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
	}

	@Test
	public void executeNewTest() {
		test = new ua.shevchuk.logic.Test();
		
		String path = command.execute(request);
		
		Assert.assertEquals("/create.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("test"));
		Assert.assertNotNull(request.getAttribute("testId"));
		Assert.assertEquals(1, request.getAttribute("questionNumber"));
		Assert.assertEquals(0, request.getSessionAttribute("size"));
	}

	@Test
	public void executeExistingTest() {
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer(0, "Answer", true, true));
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(0, "Question1", null, null, answers));
		test = new ua.shevchuk.logic.Test(0, 0, subject, user, questions);
		try {
			DaoFactory.getTestDao().create(test);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setParameter("testId", Integer.toString(test.getId()));
		
		String path = command.execute(request);
		
		Assert.assertEquals("/question.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("test"));
		Assert.assertNotNull(request.getAttribute("question"));
		Assert.assertEquals(request.getAttribute("testId"), test.getId());
		Assert.assertEquals(request.getAttribute("questionNumber"), 1);
	}

	@Test
	public void executePassedTest() {
		List<Answer> answers = new ArrayList<>();
		answers.add(new Answer(0, "Answer", true, true));
		List<Question> questions = new ArrayList<>();
		questions.add(new Question(0, "Question1", null, null, answers));
		test = new ua.shevchuk.logic.Test(0, 0, subject, user, questions);
		try {
			DaoFactory.getTestDao().create(test);
			DaoFactory.getTestDao().saveResults(user, test);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setParameter("testId", Integer.toString(test.getId()));
		
		String path = command.execute(request);
		
		Assert.assertEquals("/result.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("test"));
		Assert.assertNotNull(request.getAttribute("question"));
		Assert.assertEquals(request.getAttribute("testId"), test.getId());
		Assert.assertEquals(request.getAttribute("questionNumber"), 1);
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
