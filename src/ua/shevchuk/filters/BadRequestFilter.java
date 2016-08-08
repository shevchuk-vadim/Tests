package ua.shevchuk.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This class provides an implementation of the Filter interface, to reject incorrect HTTP requests.
 */
public class BadRequestFilter implements Filter {

	private Set<String> beforeLoginPathSet = new HashSet<>();
	private Set<String> afterLoginPathSet = new HashSet<>();
	
	/*
	 * Fills a Set with s String resource which contains comma separated path list
	 */
	private void fillPathSet(Set<String> pathSet, String resource) {
		pathSet.add("/Tests/");
		String[] pathes = resource.split(",");
		for (String path : pathes) {
			pathSet.add("/Tests/" + path.trim());
		}
	}

	/**
	 * Called by the web container to indicate to a filter that it is being placed into service.
	 * Reads a lists of correct paths of HTTP requests from resource bundle.
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("filter");
		fillPathSet(beforeLoginPathSet, resourceBundle.getString("BEFORE_LOGIN"));
		fillPathSet(afterLoginPathSet, resourceBundle.getString("AFTER_LOGIN"));
	}

	/**
	 * Called by the container each time a request/response pair is passed through the chain 
	 * due to a client request for a resource at the end of the chain.
	 * Rejects incorrect HTTP requests.
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpSession session = request.getSession();

		if (session.getAttribute("language") == null) {
			session.setAttribute("language", request.getLocale().getLanguage());
		}

		String path = request.getRequestURI();
		Set<String> pathSet = (session.getAttribute("user") == null) ? beforeLoginPathSet : afterLoginPathSet;
		if (pathSet.contains(path)) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + "/error.jsp");
		}
	}

	@Override
	public void destroy() {
	}

}
