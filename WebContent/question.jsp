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
		<form name="questionForm" method="post" action="SaveQuestion">
			<h2><fmt:message key="label.test"/> № ${test.id} <fmt:message key="label.on_subject"/> ${subject.name}</h2>
			<input type="hidden" name="testId" value="${testId}"/>
			<fmt:message key="label.number_of_questions"/>: ${test.size}
			<h3><fmt:message key="label.question"/> № ${questionNumber}</h3>
			<input type="hidden" name="questionNumber" value="${questionNumber}"/>
			<p style="white-space: pre-wrap">${question.text}</p>
			<p style="white-space: pre-wrap"><code>${question.code}</code></p>
			<ol style="list-style-type: lower-alpha">
				<c:forEach var="answer" items="${question.answers}">
					<li>
						<label>
							<input type="checkbox" name="correct${answer.id}"/>
							${answer.text}
						</label>
					</li>
				</c:forEach>
			</ol>
			<input type = "submit" value = "<fmt:message key="command.answer"/>"/>
		</form>
	</body>
</html>
