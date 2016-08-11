package ua.shevchuk.testing.commands;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import ua.shevchuk.controller.commands.DisplayTestsCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.User;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for DisplayTestsCommand class
 */
public class DisplayTestsCommandTest {

	@Test
	public void executeTest() {
		DisplayTestsCommand command = new DisplayTestsCommand();
		DataSource dataSource = new TestingDataSource();
		DaoFactory.setDataSource(dataSource);
		User user = null;
		Subject subject = null;
		try {
			user = DaoFactory.getUserDao().getWithMaxLogin();
			subject = DaoFactory.getSubjectDao().getAny();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		TestingRequestWrapper request = new TestingRequestWrapper();
		request.setParameter("subjectId", Integer.toString(subject.getId()));
		request.setSessionAttribute("user", user);
		
		String path = command.execute(request);
		
		Assert.assertEquals("/tests.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("tests"));
		Assert.assertNotNull(request.getSessionAttribute("subject"));
	}
	
}
