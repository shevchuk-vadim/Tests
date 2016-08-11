<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"/>
		<title><fmt:message key="app.name"/></title>
		<style type="text/css">
			<jsp:include page="style.css"/>
 		</style>
	</head>
	<body>
		<nav>
			<a href="index.jsp"><fmt:message key="command.home"/></a>
			<a href="DisplayTests?subjectId=${subject.id}">
				<fmt:message key="label.tests"/>
				<fmt:message key="label.on_subject"/>
				${subject.name}
			</a>
		</nav>
		
		<form name="resultForm" method="post" action="SaveQuestion">
			<header>
				<h2>
					<fmt:message key="label.test_result"/> № ${test.number}
					<fmt:message key="label.on_subject"/> ${subject.name}
					<input type="hidden" name="testId" value="${testId}"/>
				</h2>
				<fmt:message key="label.number_of_questions"/>: ${test.size} 
				<fmt:message key="label.number_of_correct_answers"/>: ${test.result}
			</header>
			
			<main>
				<h3><fmt:message key="label.question"/> № ${questionNumber}</h3>
				<input type="hidden" name="questionNumber" value="${questionNumber}"/>
				<p style="white-space: pre-wrap">${question.text}</p>
				<p style="white-space: pre-wrap"><code>${question.code}</code></p>
				
				<ol style="list-style-type: upper-alpha">
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
						<p style="color: green">
							<fmt:message key="label.answer_is_correct"/>
						</p>
					</c:when>
					<c:otherwise>
						<p style="color: red">
							<fmt:message key="label.answer_is_incorrect"/>. 
							<fmt:message key="label.correct_answer_is"/>: ${question.correct}<br/>
						</p>
					</c:otherwise>
				</c:choose>
				<c:if test="${not empty question.comment}">
					<p style="white-space: pre-wrap"><fmt:message key="label.explanation"/>: <i>${question.comment}</i></p>
				</c:if>
				<input type = "submit" value = "<fmt:message key="command.next"/>"/>
			</main>
		</form>
	</body>
</html>
