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

public class BadRequestFilter implements Filter {

	private Set<String> beforeLoginPathSet = new HashSet<>();
	private Set<String> afterLoginPathSet = new HashSet<>();
	
	private void fillPathSet(Set<String> pathSet, String resource) {
		pathSet.add("/Tests/");
		String[] pathes = resource.split(",");
		for (String path : pathes) {
			pathSet.add("/Tests/" + path.trim());
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("filter");
		fillPathSet(beforeLoginPathSet, resourceBundle.getString("BEFORE_LOGIN"));
		fillPathSet(afterLoginPathSet, resourceBundle.getString("AFTER_LOGIN"));
	}

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
