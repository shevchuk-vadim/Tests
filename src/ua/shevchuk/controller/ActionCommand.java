package ua.shevchuk.controller;

import org.apache.log4j.Logger;
import ua.shevchuk.request.RequestWrapper;

/**
 * The base class for classes that handle the HTTP requests.  
 */
public abstract class ActionCommand {

	private Logger logger = Logger.getLogger(ActionCommand.class);
	
	protected Logger getLogger() {
		return logger;
	}
	
	/**
	 * Handles the HTTP requests
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return a String specifying the pathname to the resource
	 */
	abstract public String execute(RequestWrapper request);
	
}
