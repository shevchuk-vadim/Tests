package ua.shevchuk.controller.commands;

import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/LogOut".
 */
public class LogOutCommand extends ActionCommand {

	/**
	 * Removes the user info from HTTP session attribute.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/index.jsp"
	 */
	@Override
	public String execute(RequestWrapper request) {
		request.setSessionAttribute("user", null);
		request.setSessionAttribute("test", null);
		return "/index.jsp";
	}

}
