package ua.shevchuk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import ua.shevchuk.logic.User;

/**
 * This class provides access to the table URSRS in the database.
 */
public class UserDao extends AbstractDao {

	private final String GET_USER_BY_LOGIN;
	private final String GET_USER_WITH_MAX_LOGIN;
	private final String CREATE_USER; 
	private final String DELETE_USER_BY_LOGIN;

	/**
	 * @param resourceBundle a resource bundle with SQL queries
	 * @param dataSource a data source
	 */
	public UserDao(ResourceBundle resourceBundle, DataSource dataSource) {
		super(resourceBundle, dataSource);
		
		GET_USER_BY_LOGIN = getResource("USER.GET_BY_LOGIN");
		GET_USER_WITH_MAX_LOGIN = getResource("USER.GET_WITH_MAX_LOGIN");
		CREATE_USER = getResource("USER.CREATE");
		DELETE_USER_BY_LOGIN = getResource("USER.DELETE_BY_LOGIN");
	}

	/**
	 * Gets a user by its login.
	 * @param login a user login
	 * @return a User object
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public User getByLogin(String login) throws SQLException {
		try (Connection connection = getConnection()) {
			User user = null;
			PreparedStatement statement = connection.prepareStatement(GET_USER_BY_LOGIN);
			statement.setString(1, login);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = new User(resultSet.getInt(1), login, resultSet.getString(2), resultSet.getBoolean(3));
			}
			resultSet.close();
			statement.close();
			return user;
		}
	}

	/**
	 * Gets a user with the greatest login.
	 * @return a User object
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public User getWithMaxLogin() throws SQLException {
		try (Connection connection = getConnection()) {
			User user = null;
			PreparedStatement statement = connection.prepareStatement(GET_USER_WITH_MAX_LOGIN);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getBoolean(4));
			}
			resultSet.close();
			statement.close();
			return user;
		}
	}
	
	/**
	 * Adds a new user.
	 * @param user a User object
	 * @return true if user has added successfully
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public boolean create(User user) throws SQLException {
		try (Connection connection = getConnection()) {
	    	PreparedStatement statement = connection.prepareStatement(CREATE_USER);
			statement.setString(1, user.getLogin());
			statement.setString(2, user.getPassword());
			statement.setBoolean(3, user.isTutor());
			SQLException exception = null;
			try {
				statement.execute();
			} catch (SQLException e) {
				exception = e;
			}
			statement.close();
			
			User createdUser = getByLogin(user.getLogin());
			if (exception != null) {
				if (createdUser == null) {
					throw exception;
				} else {
					createdUser = null;
				}
			}
			if (createdUser != null) {
				user.setId(createdUser.getId());
			}
			return (createdUser != null);
		}
    }

	/**
	 * Deletes a user by its login.
	 * @param login a user login
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public void deleteByLogin(String login) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_LOGIN);
			statement.setString(1, login);
			statement.execute();
			statement.close();
		}
	}

}
