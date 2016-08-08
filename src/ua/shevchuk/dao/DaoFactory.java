package ua.shevchuk.dao;

import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 * This class provides access to data objects objects.
 * Data objects objects get a resource bundle with SQL queries
 * and a data source from an instance of this class.
 * The data source is the Tomcat JDBC Connection Pool or other one
 * which is set by the method setDataSource.   
 */
public class DaoFactory {

	private final static DaoFactory INSTANCE = new DaoFactory();
	
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("sql");

	private DataSource dataSource;
	{
    	try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/tests");
		} catch (NamingException e) {
			Logger.getRootLogger().fatal("DaoFactory", e);
		}
	}

	private UserDao userDao;
	private SubjectDao subjectDao;
	private TestDao testDao;
	
	private DaoFactory() {
		init();
	}

	private void init() {
		if (dataSource != null) {
			userDao = new UserDao(resourceBundle, dataSource);
			subjectDao = new SubjectDao(resourceBundle, dataSource);
			testDao = new TestDao(resourceBundle, dataSource);
		}
	}
	
	/**
	 * Sets the data source for data access objects.
	 * @param dataSource a data source
	 */	
	public static void setDataSource(DataSource dataSource) {
		INSTANCE.dataSource = dataSource;
		INSTANCE.init();
	}
	
	/**
	 * @return a UserDao object
	 */
	public static UserDao getUserDao() {
		return INSTANCE.userDao;
	}

	/**
	 * @return a SubjectDao object.
	 */	
	public static SubjectDao getSubjectDao() {
		return INSTANCE.subjectDao;
	}

	/**
	 * @return a TestDao object.
	 */	
	public static TestDao getTestDao() {
		return INSTANCE.testDao;
	}

}
