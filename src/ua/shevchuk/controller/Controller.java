package ua.shevchuk.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import ua.shevchuk.request.WorkingRequestWrapper;

/**
 * Extends the Servlet HttpServlet abstract class and owerrides methods
 * for HTTP GET and POST requests. These methods call the execute method of class
 * that extends the ActionCommand abstract class, which is selected by CommandFactory.
 * @see ActionCommand
 * @see CommandFactory
 */
@SuppressWarnings("serial")
public class Controller extends HttpServlet {
	
    /**
     * Called by the servlet container to indicate to a servlet that the servlet is being placed into service.
     * Configured the log4j logging. 
     */
	@Override
	public void init () {
		String path = getServletContext().getRealPath("/WEB-INF/log4j.xml");
		new DOMConfigurator().doConfigure(path, LogManager.getLoggerRepository());
	}
	
    /*
     * Called by the server to allow a servlet to handle a GET request.
     */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ActionCommand command = CommandFactory.getCommand(request.getRequestURI());
    	String path = command.execute(new WorkingRequestWrapper(request));
    	request.getRequestDispatcher(path).forward(request, response);
	}

    /*
     * Called by the server to allow a servlet to handle a POST request.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
