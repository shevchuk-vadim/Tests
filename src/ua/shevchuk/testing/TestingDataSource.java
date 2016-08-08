package ua.shevchuk.testing;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * This class provides an implementation of the DataSource interface 
 * and represents an alternative data source for data access objects,
 * when the Tomcat JDBC Connection Pool is not working.   
 */
public class TestingDataSource implements DataSource {
	
	private String url;
	private String username;
	private String password;
	
	public TestingDataSource() { 
		ResourceBundle resourceBundle = ResourceBundle.getBundle("testconnection");
		String driverClassName = resourceBundle.getString("driverClassName");
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		url = resourceBundle.getString("url");
		username = resourceBundle.getString("username");
		password = resourceBundle.getString("password");
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
	}

	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

}
