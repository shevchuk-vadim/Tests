package ua.shevchuk.logic;

/**
 * This class is a subject in the system of testing. 
 */
public class Subject {

	private int id;
	private String name;
	private String language;
	
	public Subject(int id, String name, String language) {
		this.id = id;
		this.name = name;
		this.language = language;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
