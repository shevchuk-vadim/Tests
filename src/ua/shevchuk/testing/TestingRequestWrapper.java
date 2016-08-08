package ua.shevchuk.testing;

import java.util.HashMap;
import java.util.Map;
import ua.shevchuk.request.RequestWrapper;

/**
 * This class provides an implementation of the RequestWrapper interface,
 * and works without an instance of HttpServletRequest wrapped class for testing purposes.
 */
public class TestingRequestWrapper implements RequestWrapper {

	private Map<String, String> parameterMap = new HashMap<>(); 
	private Map<String, Object> attributeMap = new HashMap<>();
	private Map<String, Object> sessionAttributeMap = new HashMap<>();
	
	/**
	 * @param name a String specifying the name of the parameter
	 * @param value a String specifying the value of the parameter
	 */
	public void setParameter(String name, String value) {
		parameterMap.put(name, value);
	}

	@Override
	public String getParameter(String name) {
		return parameterMap.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		attributeMap.put(name, value);
	}

	/**
	 * @param name name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if the attribute does not exist
	 */
	public Object getAttribute(String name) {
		return attributeMap.get(name);
	}

	@Override
	public void setSessionAttribute(String name, Object value) {
		sessionAttributeMap.put(name, value);
	}

	@Override
	public Object getSessionAttribute(String name) {
		return sessionAttributeMap.get(name);
	}

}
