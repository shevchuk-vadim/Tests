package ua.shevchuk.logic;

import java.util.List;

/**
 * This class is a question of test in the system of testing. 
 */
public class Question {

	private int id;
	private String text;
	private String code;
	private String comment;
	private List<Answer> answers;

	public Question() {
	}

	public Question(int id, String text, String code, String comment, List<Answer> answers) {
		this.id = id;
		this.text = text;
		this.code = code;
		this.comment = comment;
		this.answers = answers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * @return true if an user answered this question correctly
	 */
	public boolean getResult() {
		for (Answer answer : answers) {
			if (answer.getResult() != answer.isCorrect()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return true if an user has answered this question
	 */
	public boolean isAnswered() {
		for (Answer answer : answers) {
			if (answer.getResult() != false) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return list of correct variants of answer for this question as a String,
	 * in upper-alpha style (A, B, C, etc.)
	 */
	public String getCorrect() {
		StringBuilder buf = new StringBuilder();
		char c = 'A';
		for (Answer answer : answers) {
			if (answer.isCorrect()) {
				buf.append(c);
			}
			c++;
		}
		return new String(buf);
	}
	
}
