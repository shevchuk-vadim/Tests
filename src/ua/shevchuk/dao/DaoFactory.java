package ua.shevchuk.dao;

import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

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
	
	public static void setDataSource(DataSource dataSource) {
		INSTANCE.dataSource = dataSource;
		INSTANCE.init();
	}
	
	public static UserDao getUserDao() {
		return INSTANCE.userDao;
	}

	public static SubjectDao getSubjectDao() {
		return INSTANCE.subjectDao;
	}

	public static TestDao getTestDao() {
		return INSTANCE.testDao;
	}

}
