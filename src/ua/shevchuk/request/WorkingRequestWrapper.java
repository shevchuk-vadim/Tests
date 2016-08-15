package ua.shevchuk.request;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * This class provides an implementation of the RequestWrapper interface,
 * and gets an instance of HttpServletRequest wrapped class in the constructor.
 */
public class WorkingRequestWrapper implements RequestWrapper {

	HttpServletRequest request;
	
	/**
	 * @param request a wrapped HttpServletRequest object
	 */
	public WorkingRequestWrapper(HttpServletRequest request) {
		this.request = request;
		try {
			this.request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.getRootLogger().error("WorkingRequestWrapper", e);
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
