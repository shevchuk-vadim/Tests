package ua.shevchuk.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.sql.DataSource;

/**
 * The base class for DAO classes.
 * It provides them SQL queries from resource bundle and connections from data source. 
 */
public abstract class AbstractDao {

	private ResourceBundle resourceBundle;
	private DataSource dataSource;
	
	/**
	 * @param resourceBundle a resource bundle with SQL queries
	 * @param dataSource a data source
	 */
	public AbstractDao(ResourceBundle resourceBundle, DataSource dataSource) {
		this.dataSource = dataSource;
		this.resourceBundle = resourceBundle;
	}
	
	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	protected String getResource(String key) {
		return resourceBundle.getString(key);
	}
	
}
