package ua.shevchuk.testing.commands;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.shevchuk.controller.commands.RegisterCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.User;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for RegisterCommand class
 */
public class RegisterCommandTest {

	private RegisterCommand command;
	private DataSource dataSource;
	private User user;
	private TestingRequestWrapper request;			
	
	@Before
	public void init() {
		command = new RegisterCommand();
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
		request.setParameter("login", user.getLogin() + "1");
		request.setParameter("password", "1");
		request.setParameter("verifyPassword", "1");

		String path = command.execute(request);
		
		Assert.assertEquals("/login.jsp", path);
	}

	@Test
	public void executeDifferentPasswordsTest() {
		request.setParameter("login", user.getLogin());
		request.setParameter("password", "1");
		request.setParameter("verifyPassword", "2");
		
		String path = command.execute(request);
		user = null;
		
		Assert.assertEquals("/registration.jsp", path);
		Assert.assertNotNull(request.getAttribute("errorMessage"));
		Assert.assertNotNull(request.getAttribute("login"));
	}

	@Test
	public void executeLoginExistsTest() {
		request.setParameter("login", user.getLogin());
		request.setParameter("password", "1");
		request.setParameter("verifyPassword", "1");
		
		String path = command.execute(request);
		user = null;
		
		Assert.assertEquals("/registration.jsp", path);
		Assert.assertNotNull(request.getAttribute("errorMessage"));
		Assert.assertNotNull(request.getAttribute("login"));
	}

	@After
	public void destroy() {
		if (user != null) {
			try {
				DaoFactory.getUserDao().deleteByLogin(user.getLogin() + "1");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
