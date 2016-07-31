package ua.shevchuk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import ua.shevchuk.logic.Subject;

public class SubjectDao {
	
	private final String GET_SUBJECTS_BY_LENGUAGE;
	private final String GET_SUBJECT; 
	private final String GET_ANY_SUBJECT; 

	private DataSource dataSource;
	private final Map<Integer, Subject> map = new ConcurrentHashMap<>();
	
	public SubjectDao(ResourceBundle resourceBundle, DataSource dataSource) {
		GET_SUBJECTS_BY_LENGUAGE = resourceBundle.getString("SUBJECT.GET_LIST_BY_LANGUAGE");
		GET_SUBJECT = resourceBundle.getString("SUBJECT.GET");
		GET_ANY_SUBJECT = resourceBundle.getString("SUBJECT.GET_ANY");
		
		this.dataSource = dataSource;
	}

    public List<Subject> getListByLanguage(String language) throws SQLException {
  		try (Connection connection = dataSource.getConnection()) {
	    	ArrayList<Subject> list = new ArrayList<>();
			PreparedStatement statement = connection.prepareStatement(GET_SUBJECTS_BY_LENGUAGE);
			statement.setString(1, language);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int subjectId = resultSet.getInt(1);
				Subject subject = new Subject(subjectId, resultSet.getString(2), language); 
				list.add(subject);
				map.put(subjectId, subject);
			}
			statement.close();
			return list;
		}
    }

	public Subject get(int id) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			Subject subject = map.get(id);
			if (subject == null) {
				PreparedStatement statement = connection.prepareStatement(GET_SUBJECT);
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					subject = new Subject(id, resultSet.getString(1), resultSet.getString(2));
					map.put(id, subject);
				}
				resultSet.close();
				statement.close();
			}
			return subject;
		}
	}

	public Subject getAny() throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			Subject subject = null;
			PreparedStatement statement = connection.prepareStatement(GET_ANY_SUBJECT);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				subject = new Subject(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
			}
			resultSet.close();
			statement.close();
			return subject;
		}
	}

}
