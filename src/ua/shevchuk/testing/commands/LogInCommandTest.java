package ua.shevchuk.testing.commands;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.shevchuk.controller.commands.LogInCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.User;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for LogInCommand class
 */
public class LogInCommandTest {

	private LogInCommand command;
	private DataSource dataSource;
	private User user;
	private TestingRequestWrapper request;			
	
	@Before
	public void init() {
		command = new LogInCommand();
		dataSource = new TestingDataSource();
		DaoFactory.setDataSource(dataSource);
		try {
			user = DaoFactory.getUserDao().getWithMaxLogin();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		request = new TestingRequestWrapper();			
	}

	@Test
	public void executeCorrectTest() {
		request.setParameter("login", user.getLogin());
		request.setParameter("password", user.getPassword());

		String path = command.execute(request);
		
		Assert.assertEquals("/index.jsp", path);
		Assert.assertNotNull(request.getSessionAttribute("user"));
		Assert.assertNotNull(request.getSessionAttribute("subjects"));
	}

	@Test
	public void executeIncorrectPasswordTest() {
		request.setParameter("login", user.getLogin());
		request.setParameter("password", user.getPassword() + "1");
		
		String path = command.execute(request);
		
		Assert.assertEquals("/login.jsp", path);
		Assert.assertNotNull(request.getAttribute("errorMessage"));
		Assert.assertNotNull(request.getAttribute("login"));
	}

	@Test
	public void executeIncorrectLoginTest() {
		request.setParameter("login", user.getLogin() + "1");
		request.setParameter("password", user.getPassword());
		
		String path = command.execute(request);
		
		Assert.assertEquals("/login.jsp", path);
		Assert.assertNotNull(request.getAttribute("errorMessage"));
		Assert.assertNotNull(request.getAttribute("login"));
	}

}
