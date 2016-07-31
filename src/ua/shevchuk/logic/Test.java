package ua.shevchuk.logic;

import java.util.List;

public class Test {

	private int id;
	private Subject subject;
	private User user;
	private List<Question> questions;
	boolean passed;
	
	public Test() {}
	
	public Test(int id, Subject subject, User user, List<Question> questions) {
		this.id = id;
		this.subject = subject;
		this.user = user;
		this.questions = questions;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public int getSize() {
		return questions.size();
	}

	public int getResult() {
		int result = 0;
		for (Question question : questions) {
			if (question.getResult() == true) {
				result++;
			}
		}
		return result;
	}
	
}
