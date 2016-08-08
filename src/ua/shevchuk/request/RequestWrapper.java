package ua.shevchuk.request;

/**
 * An object that acts as a wrapper for the HttpServletRequest. 
 */
public interface RequestWrapper {

	/**
	 * Gets the value of a HttpServletRequest parameter.
	 * @param name a String specifying the name of the parameter
	 * @return a String representing the value of the parameter, or null if the parameter does not exist
	 */
	String getParameter(String name);

	/**
	 * Gets the value of a HttpServletRequest parameter as int.
	 * @param name a String specifying the name of the parameter
	 * @return a int representing the value of the parameter
	 * or 0 if the parameter does not exist or cannot be converted to int 
	 */
	default int getIntParameter(String name) {
		String value = getParameter(name);
		int result = 0;
		if (value != null) {
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException e) {}
		}
		return result;
	}

	/**
	 * Sets the value of a HttpServletRequest attribute.
	 * @param name a String specifying the name of the attribute
	 * @param value an Object containing the value of the attribute
	 */
	void setAttribute(String name, Object value);
	
	/**
	 * Sets the value of a HttpServletRequest attribute named "errorMessage".
	 * @param value a String specifying the value of the attribute
	 */
	default void setAttributeErrorMessage(String value) {
		setAttribute("errorMessage", value);
	}

	/**
	 * Sets the value of an attribute of current HttpSession object associated with the request.
	 * @param name a String specifying the name of the attribute
	 * @param value an Object containing the value of the attribute
	 */
	void setSessionAttribute(String name, Object value);
	
	/**
	 * Gets the value of an attribute of current HttpSession object associated with the request.
	 * @param name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if the attribute does not exist
	 */
	Object getSessionAttribute(String name);
	
}
