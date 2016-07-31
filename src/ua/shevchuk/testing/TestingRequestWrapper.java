package ua.shevchuk.testing;

import java.util.HashMap;
import java.util.Map;
import ua.shevchuk.request.RequestWrapper;

public class TestingRequestWrapper implements RequestWrapper {

	private Map<String, String> parameterMap = new HashMap<>(); 
	private Map<String, Object> attributeMap = new HashMap<>();
	private Map<String, Object> sessionAttributeMap = new HashMap<>();
	
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
