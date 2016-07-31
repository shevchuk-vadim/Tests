<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title><fmt:message key="app.name"/></title>
	</head>
	<body>
		<a href="index.jsp"><fmt:message key="command.home"/></a>
		<a href="DisplayTests?subjectId=${subject.id}"><fmt:message key="command.test_list"/></a>
		<form name="resultForm" method="post" action="SaveQuestion">
			<h2><fmt:message key="label.test_result"/> № ${test.id} <fmt:message key="label.on_subject"/> ${subject.name}</h2>
			<input type="hidden" name="testId" value="${testId}"/>
			<fmt:message key="label.number_of_questions"/>: ${test.size} 
			<fmt:message key="label.number_of_correct_answers"/>: ${test.result}
			<h3><fmt:message key="label.question"/> № ${questionNumber}</h3>
			<input type="hidden" name="questionNumber" value="${questionNumber}"/>
			<p style="white-space: pre-wrap">${question.text}</p>
			<p style="white-space: pre-wrap"><code>${question.code}</code></p>
			<ol style="list-style-type: lower-alpha">
				<c:forEach var="answer" items="${question.answers}">
					<li>
						<label>
							<input type="checkbox" name="correct${answer.id}" disabled <c:if test="${answer.result}">checked</c:if>/>
							${answer.text}
						</label>
					</li>
				</c:forEach>
			</ol>
			<c:choose>
				<c:when test="${question.result}">
					<fmt:message key="label.answer_is_correct"/><br/>
				</c:when>
				<c:otherwise>
					<fmt:message key="label.answer_is_incorrect"/>. 
					<fmt:message key="label.correct_answer_is"/>: ${question.correct}<br/>
				</c:otherwise>
			</c:choose>
			<c:if test="${not empty question.comment}">
				<p style="white-space: pre-wrap"><i><fmt:message key="label.explanation"/>: ${question.comment}</i></p>
			</c:if>
			<input type = "submit" value = "<fmt:message key="command.next"/>"/>
		</form>
	</body>
</html>
