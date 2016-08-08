package ua.shevchuk.testing.commands;

import org.junit.Test;
import javax.sql.DataSource;
import org.junit.Assert;
import ua.shevchuk.controller.commands.SetLanguageCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.testing.TestingDataSource;
import ua.shevchuk.testing.TestingRequestWrapper;

/**
 * JUnit tests for SetLanguageCommand class
 */
public class SetLanguageCommandTest {

	@Test
	public void executeTest() {
		SetLanguageCommand command = new SetLanguageCommand();
		DataSource dataSource = new TestingDataSource();
		DaoFactory.setDataSource(dataSource);
		TestingRequestWrapper request = new TestingRequestWrapper();
		String language = "ru";
		request.setParameter("language", language);
		String path = command.execute(request);
		Assert.assertEquals("/index.jsp", path);
		Assert.assertEquals(language, request.getSessionAttribute("language"));
		Assert.assertNotNull(request.getSessionAttribute("subjects"));
	}
	
}
