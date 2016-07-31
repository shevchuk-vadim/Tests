package ua.shevchuk.testing.commands;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.shevchuk.controller.commands.StartTestingCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.User;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

public class StartTestingCommandTest {

	private StartTestingCommand command;
	private DataSource dataSource;
	private User user;
	private Subject subject;
	private TestingRequestWrapper request;			
	
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
	}

	@Test
	public void executeNewTest() {
		request.setSessionAttribute("user", user);
		request.setSessionAttribute("subject", subject);
		String path = command.execute(request);
		Assert.assertEquals("/create.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("test"));
		Assert.assertNotNull(request.getAttribute("testId"));
		Assert.assertEquals(1, request.getAttribute("questionNumber"));
		Assert.assertEquals(1, request.getSessionAttribute("size"));
	}

	@Test
	public void executeNotNewTest() {
		int testId = 0;
		try {
			List<ua.shevchuk.logic.Test> tests = DaoFactory.getTestDao().getListBySubject(user, subject);
			if (tests.size() > 0) {
				testId = tests.get(0).getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setParameter("testId", Integer.toString(testId));
		request.setSessionAttribute("user", user);
		request.setSessionAttribute("subject", subject);
		String path = command.execute(request);
		ua.shevchuk.logic.Test test = (ua.shevchuk.logic.Test) request.getSessionAttribute("test");
		Assert.assertNotNull(test);
		Assert.assertEquals(test.isPassed() ? "/result.jsp" : "/question.jsp", path);
		Assert.assertNotNull(request.getAttribute("testId"));
		Assert.assertEquals(1, request.getAttribute("questionNumber"));
		Assert.assertNotNull(request.getAttribute("question"));
	}

}
