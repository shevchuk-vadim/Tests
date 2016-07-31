package ua.shevchuk.logic;

public class Answer {
	
	private int id;
	private String text;
	private boolean correct;
	private boolean result;
	
	public Answer() {
	}
	
	public Answer(int id, String text, boolean correct, boolean result) {
		this.id = id;
		this.text = text;
		this.correct = correct;
		this.result = result;
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
	
	public boolean isCorrect() {
		return correct;
	}
	
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	public boolean getResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}

}
