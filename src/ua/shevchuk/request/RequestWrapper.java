package ua.shevchuk.request;

public interface RequestWrapper {

	String getParameter(String name);

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

	void setAttribute(String name, Object value);
	
	default void setAttributeErrorMessage(Object value) {
		setAttribute("errorMessage", value);
	}

	void setSessionAttribute(String name, Object value);
	
	Object getSessionAttribute(String name);
	
}
