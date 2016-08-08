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

/**
 * This class provides access to the table SUBJECTS in the database.
 */
public class SubjectDao extends AbstractDao {
	
	private final String GET_SUBJECTS_BY_LENGUAGE;
	private final String GET_SUBJECT; 
	private final String GET_ANY_SUBJECT; 

	private final Map<Integer, Subject> map = new ConcurrentHashMap<>();
	
	/**
	 * @param resourceBundle a resource bundle with SQL queries
	 * @param dataSource a data source
	 */
	public SubjectDao(ResourceBundle resourceBundle, DataSource dataSource) {
		super(resourceBundle, dataSource);
		
		GET_SUBJECTS_BY_LENGUAGE = getResource("SUBJECT.GET_LIST_BY_LANGUAGE");
		GET_SUBJECT = getResource("SUBJECT.GET");
		GET_ANY_SUBJECT = getResource("SUBJECT.GET_ANY");
	}

	/**
	 * Gets a list of subjects for a given language.
	 * @param language language code as a string like "en"
	 * @return a List view of the subjects for a given language
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public List<Subject> getListByLanguage(String language) throws SQLException {
  		try (Connection connection = getConnection()) {
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

	/**
	 * Gets a subject by its identifier.
	 * @param id a subject identifier
	 * @return a Subject object
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public Subject get(int id) throws SQLException {
		try (Connection connection = getConnection()) {
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

	/**
	 * Gets the first found subject.
	 * @return a Subject object
	 * @throws SQLException if an error occurred while accessing the database
	 */
	public Subject getAny() throws SQLException {
		try (Connection connection = getConnection()) {
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
