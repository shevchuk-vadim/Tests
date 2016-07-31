package ua.shevchuk.request;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WorkingRequestWrapper implements RequestWrapper {

	HttpServletRequest request;
	
	public WorkingRequestWrapper(HttpServletRequest request) {
		this.request = request;
		try {
			this.request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}

	@Override
	public void setSessionAttribute(String name, Object value) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute(name, value);
		}
	}

	@Override
	public Object getSessionAttribute(String name) {
		HttpSession session = request.getSession();
		if (session != null) {
			return session.getAttribute(name);
		}
		return null;
	}

}
