package ua.shevchuk.controller.commands;
	
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ua.shevchuk.controller.ActionCommand;
import ua.shevchuk.dao.DaoFactory;
import ua.shevchuk.logic.Answer;
import ua.shevchuk.logic.Question;
import ua.shevchuk.logic.Subject;
import ua.shevchuk.logic.Test;
import ua.shevchuk.logic.User;
import ua.shevchuk.request.RequestWrapper;

/**
 * Handles the HTTP request with URI "/Tests/SaveQuestion".
 */
public class SaveQuestionCommand extends ActionCommand {

	/**
	 * Put the question info to HTTP session attribute if the tutor has created a new question
	 * or the student has answered the current question. Increments by one the number current question
	 * and put it to HTTP request attribute. Saves the new test or results of passed test
	 * in the database if the current question was the last question in the test.
	 * @param request a RequestWrapper object that acts as a wrapper for the HttpServletRequest
	 * @return "/create.jsp" if the user has created a new question;
	 * "/question.jsp" if the user has answered a question;
	 * "/result.jsp" if the user has watched the result of question or has answered the last question in the test;
	 * "/tests.jsp" if user has created the last question in the new test
	 * or has watched the result of the last question in the test;
	 * "/error.jsp" the was an error while accessing the database or processing the request
	 */
	public String execute(RequestWrapper request) {
		Test test = (Test) request.getSessionAttribute("test");
		int testId = request.getIntParameter("testId");
		/*verification of the resend of form since passing the test was complete*/
		if (test.getId() != testId) {
			request.setAttributeErrorMessage("error.test_is_interrupted");
			return "/error.jsp";
		}
		
		int questionNumber = request.getIntParameter("questionNumber");
		User user = (User) request.getSessionAttribute("user");
		Subject subject = (Subject) request.getSessionAttribute("subject");
		try  {			
			if (testId == 0) {
				/*get the new question*/
				String text = request.getParameter("text");
				String code = request.getParameter("code");
				String comment = request.getParameter("comment");
				List<Answer> answers = new ArrayList<>();
				for (int i = 1; i <= 8; i++) {
					String answerTest = request.getParameter("text" + i);
					if (answerTest.length() == 0) {
						break;
					}
					boolean isCorrectAnswer = (request.getParameter("correct" + i) != null);
					answers.add(new Answer(0, answerTest, isCorrectAnswer, false));
				}
				Question question = new Question(0, text, code, comment, answers);
				if (questionNumber > test.getQuestions().size()) {
					test.getQuestions().add(question);
				} else {
					test.getQuestions().set(questionNumber - 1, question);
				}
	
				int size = request.getIntParameter("size");
				if (questionNumber < size) {
					/*move to the new question*/
					request.setAttribute("size", size);
					request.setAttribute("questionNumber", ++questionNumber);
					return "/create.jsp";
				} else {
					/*save new test and move to list of tests*/
					DaoFactory.getTestDao().create(test);
					getLogger().info("User " + user.getLogin() + " has created the test # " + test.getId());

					List<Test> tests = DaoFactory.getTestDao().getListBySubject(user, subject);
					request.setSessionAttribute("tests", tests);
					return "/tests.jsp";
				}
			} else if (!test.isPassed()) {
				/*get the result or question*/
				Question question = test.getQuestions().get(questionNumber - 1);
				for (Answer answer : question.getAnswers()) {
					boolean isCorrectAnswer = (request.getParameter("correct" + answer.getId()) != null);
					answer.setResult(isCorrectAnswer);
				}
	
				if (questionNumber < test.getSize()) {
					/*move to the new question*/
					question = test.getQuestions().get(questionNumber);
					request.setAttribute("question", question);
					request.setAttribute("testId", testId);
					request.setAttribute("questionNumber", ++questionNumber);
					return "/question.jsp";
				} else {
					/*save the result of test and move to watching the result of test*/
					DaoFactory.getTestDao().saveResults(user, test);
					getLogger().info("User " + user.getLogin() + " has passed the test # " + test.getId());
					test.setPassed(true);

					question = test.getQuestions().get(0);
					request.setAttribute("question", question);
					request.setAttribute("testId", testId);
					request.setAttribute("questionNumber", 1);
					return "/result.jsp";
				}
			} else {
				if (questionNumber < test.getSize()) {
					/*move to the new question*/
					Question question = test.getQuestions().get(questionNumber);
					request.setAttribute("question", question);
					request.setAttribute("testId", test.getId());
					request.setAttribute("questionNumber", ++questionNumber);
					return "/result.jsp";
				} else {
					/*move to list of tests*/
					List<Test> tests = DaoFactory.getTestDao().getListBySubject(user, subject);
					request.setSessionAttribute("tests", tests);
					return "/tests.jsp";
				}
			}
		} catch (SQLException e) {
			getLogger().error("SaveQuestionCommand", e);
			request.setAttributeErrorMessage("error.accessing_database");
			return "/error.jsp";
		}  catch (IndexOutOfBoundsException e) {
			getLogger().error("SaveQuestionCommand", e);
			request.setAttributeErrorMessage("error.processing_request");
			return "/error.jsp";
		}
	}

}
