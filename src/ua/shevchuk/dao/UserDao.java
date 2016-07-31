package ua.shevchuk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import ua.shevchuk.logic.User;

public class UserDao {

	private final String GET_USER_BY_LOGIN;
	private final String GET_USER_WITH_MAX_LOGIN;
	private final String CREATE_USER; 
	private final String DELETE_USER_BY_LOGIN;
	
	private DataSource dataSource;
	
	public UserDao(ResourceBundle resourceBundle, DataSource dataSource) {
		GET_USER_BY_LOGIN = resourceBundle.getString("USER.GET_BY_LOGIN");
		GET_USER_WITH_MAX_LOGIN = resourceBundle.getString("USER.GET_WITH_MAX_LOGIN");
		CREATE_USER = resourceBundle.getString("USER.CREATE");
		DELETE_USER_BY_LOGIN = resourceBundle.getString("USER.DELETE_BY_LOGIN");

		this.dataSource = dataSource;
	}

	public User getByLogin(String login) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
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

	public User getWithMaxLogin() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
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
	
    public boolean create(User user) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
	    	PreparedStatement statement = connection.prepareStatement(CREATE_USER);
			statement.setString(1, user.getLogin());
			statement.setString(2, user.getPassword());
			statement.setBoolean(3, user.isTutor());
			try {
				statement.execute();
			} catch (SQLException e) {
				try {
					if (getByLogin(user.getLogin()) != null) {
						return false;
					}
				} catch (SQLException e1) {}
				throw e;
			}
			statement.close();
			return true;
		}
    }

	public void deleteByLogin(String login) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_LOGIN);
			statement.setString(1, login);
			statement.execute();
			statement.close();
		}
	}

}
