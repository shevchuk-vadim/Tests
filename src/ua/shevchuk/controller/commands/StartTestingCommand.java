package ua.shevchuk.controller.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.Test;
import ua.shevchuk.logic.User;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/StartTesting".
 */
public class StartTestingCommand extends ActionCommand {

	/**
	 * Creates a new instance of Test class for new or existing test and it to HTTP session attribute.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/create.jsp" if the user has started creating a new test;
	 * "/question.jsp" if the user has started passing a test;
	 * "/result.jsp" if the user has started watching the result of test;
	 * "/error.jsp" the was an error while accessing the database or processing the request
	 */
	@Override
	public String execute(RequestWrapper request) {
		int testId = request.getIntParameter("testId");
		Test test = null;		
		User user = (User) request.getSessionAttribute("user");
		Subject subject = (Subject) request.getSessionAttribute("subject");
		if (testId == 0) {
			test = new Test(testId, subject, user, new ArrayList<>());
		} else {
			try {
				test = DaoFactory.getTestDao().get(user, subject, testId);
			} catch (SQLException e) {
				getLogger().error("StartTestingCommand", e);
				request.setAttributeErrorMessage("error.accessing_database");
				return "/error.jsp";
			}
		}
		try {
			testId = test.getId();
		} catch (NullPointerException e) {
			getLogger().error("StartTestingCommand", e);
			request.setAttributeErrorMessage("error.processing_request");
			return "/error.jsp";
		}

		request.setSessionAttribute("test", test);
		request.setAttribute("testId", testId);
		request.setAttribute("questionNumber", 1);
		if (testId == 0) {
			request.setSessionAttribute("size", 1);
			return "/create.jsp";
		} else if (!test.isPassed()) {
			request.setAttribute("question", test.getQuestions().get(0));
			return "/question.jsp";
		} else {
			request.setAttribute("question", test.getQuestions().get(0));
			return "/result.jsp";
		}
	}

}
