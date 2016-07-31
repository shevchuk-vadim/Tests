package ua.shevchuk.controller.commands;

import java.sql.SQLException;
import java.util.List;
import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.Test;
import ua.shevchuk.logic.User;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/DisplayTests".
 */
public class DisplayTestsCommand extends ActionCommand {

	/**
	 * Prepares then list of tests after selection of subject and put it to HTTP session attribute.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/tests.jsp", or "/error.jsp" if the was an error while accessing the database or processing the request
	 */
	@Override
	public String execute(RequestWrapper request) {
		try {
			int subjectId = request.getIntParameter("subjectId");
			Subject subject = DaoFactory.getSubjectDao().get(subjectId);
			try {
				subjectId = subject.getId();
			} catch (NullPointerException e) {
				getLogger().error("DisplayTestsCommand", e);
				request.setAttributeErrorMessage("error.processing_request");
				return "/error.jsp";
			}
			
			User user = (User) request.getSessionAttribute("user");
			List<Test> tests = DaoFactory.getTestDao().getListBySubject(user, subject);
			request.setSessionAttribute("tests", tests);
			request.setSessionAttribute("subject", subject);
			return "/tests.jsp";
		} catch (SQLException e) {
			getLogger().error("DisplayTestsCommand", e);
			request.setAttributeErrorMessage("error.accessing_database");
			return "/error.jsp";
		}
	}

}
