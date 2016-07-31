package ua.shevchuk.controller.commands;

import java.sql.SQLException;
import java.util.List;
import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.User;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/LogIn".
 */
public class LogInCommand extends ActionCommand {

	/**
	 * Identifies an user on a login and password and put the user info to HTTP session attribute.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/index.jsp", or "/login.jsp" if the login or password were incorrect, 
	 * or "/error.jsp" if the was an error while accessing the database
	 */
	@Override
	public String execute(RequestWrapper request) {
		try {
			String login = request.getParameter("login");
			User user = DaoFactory.getUserDao().getByLogin(login);
			if (user == null) {
				request.setAttributeErrorMessage("error.incorrect_login");
				request.setAttribute("login", login);
				return "/login.jsp";
			} else if (!user.getPassword().equals(request.getParameter("password"))) {
				request.setAttributeErrorMessage("error.incorrect_password");
				request.setAttribute("login", login);
				return "/login.jsp";
			} else {
				String language = (String) request.getSessionAttribute("language");
				List<Subject> subjects = DaoFactory.getSubjectDao().getListByLanguage(language);
				request.setSessionAttribute("user", user);
				request.setSessionAttribute("subjects", subjects);
				return "/index.jsp";
			}
		} catch (SQLException e) {
			getLogger().error("LogInCommand", e);
			request.setAttributeErrorMessage("error.accessing_database");
			return "/error.jsp";
		}
	}

}
