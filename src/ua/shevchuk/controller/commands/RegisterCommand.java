package ua.shevchuk.controller.commands;

import java.sql.SQLException;
import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.User;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/Register".
 */
public class RegisterCommand extends ActionCommand {

	/**
	 * Saves the new user info in the database.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/login.jsp", or "/registration.jsp" if the login or password were incorrect, 
	 * or "/error.jsp" if the was an error while accessing the database
	 */
	@Override
	public String execute(RequestWrapper request) {
		try {
			String login = request.getParameter("login");
			String password = request.getParameter("password");
			boolean isTutor = (request.getParameter("isTutor") != null);
			request.setAttribute("login", login);
			if (!request.getParameter("repeatPassword").equals(password)) {
				request.setAttribute("isTutor", isTutor);
				request.setAttributeErrorMessage("error.password_verification");
				return "/registration.jsp";
			} else if (!DaoFactory.getUserDao().create(new User(0, login, password, isTutor))) {
				request.setAttribute("isTutor", isTutor);
				request.setAttributeErrorMessage("error.login_exists");
				return "/registration.jsp";
			} else {
				getLogger().info("User " + login + " has registered");
				return "/login.jsp";
			}
		} catch (SQLException e) {
			getLogger().error("RegisterCommand", e);
			request.setAttributeErrorMessage("error.accessing_database");
			return "/error.jsp";
		}
	}

}
