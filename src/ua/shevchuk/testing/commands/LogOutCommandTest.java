package ua.shevchuk.testing.commands;

import org.junit.Assert;
import org.junit.Test;
import ua.shevchuk.controller.commands.LogOutCommand;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for LogOutCommand class
 */
public class LogOutCommandTest {

	@Test
	public void executeTest() {
		LogOutCommand command = new LogOutCommand();
		TestingRequestWrapper request = new TestingRequestWrapper();
		
		String path = command.execute(request);
		
		Assert.assertEquals("/index.jsp", path);
		Assert.assertNull(request.getParameter("user"));
		Assert.assertNull(request.getParameter("test"));
	}

}
