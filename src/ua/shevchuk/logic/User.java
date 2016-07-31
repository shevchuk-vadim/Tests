package ua.shevchuk.logic;

public class User {
	
	private int id;
	private String login;
	private String password;
	private boolean tutor;

	public User(int id, String login, String password, boolean tutor) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.tutor = tutor;
	}

	public int getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isTutor() {
		return tutor;
	}

	public void setTutor(boolean tutor) {
		this.tutor = tutor;
	}

}
