package ua.shevchuk.controller.commands;

import java.sql.SQLException;
import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.dao.SubjectDao;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/SetLanguage".
 */
public class SetLanguageCommand extends ActionCommand {

	/**
	 * Prepares then list of tests after selection of language and put it to HTTP session attribute.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/index.jsp", or "/error.jsp" if the was an error while accessing the database
	 */
	@Override
	public String execute(RequestWrapper request) {
		String language = request.getParameter("language");
		request.setSessionAttribute("language", language);
		SubjectDao subjectDao = DaoFactory.getSubjectDao();
		try {
			request.setSessionAttribute("subjects", subjectDao.getListByLanguage(language));
		} catch (SQLException e) {
			getLogger().error("SetLanguageCommand", e);
			request.setAttributeErrorMessage("error.accessing_database");
			return "/error.jsp";
		}
		return "/index.jsp";
	}

}
