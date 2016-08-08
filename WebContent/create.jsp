<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/custom.tld" prefix="ctg" %>
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
			<h2><fmt:message key="label.new_test"/> <fmt:message key="label.on_subject"/> ${subject.name}</h2>
			<input type="hidden" name="testId" value="0"/>
			<fmt:message key="label.number_of_questions"/> (<fmt:message key="label.to_number"/> 50): 
			<input type="text" name="size" required size="3" style="text-align: center" maxlength="2" 
				pattern="[1-9]|[1-4][0-9]|50" value="${size}"/>
			<h3><fmt:message key="label.question"/> № ${questionNumber}</h3>
			<input type="hidden" name="questionNumber" value="${questionNumber}"/>
			<textarea name="text" required rows="2" cols="80" maxlength="500"></textarea><br/>
			<fmt:message key="label.program_code"/> (<fmt:message key="label.if_required"/>):<br/>
			<textarea name="code" rows="10" cols="80" maxlength="4000"></textarea><br/>
			<fmt:message key="label.answers_to_question"/> 
			(<fmt:message key="label.from_number"/> 2 <fmt:message key="label.to_number"/> 8):<br/>
			<ol style="list-style-type: upper-alpha">
				<ctg:repeat number="8" varCount="count">
					<li>
						<input type="checkbox" name="correct${count}"/>
						<input type="text" name="text${count}" <c:if test="${count<=2}">required</c:if> size="65" maxlength="500"/>
					</li>
				</ctg:repeat>
			</ol>
			<fmt:message key="label.explanation"/> (<fmt:message key=	"label.if_required"/>):<br/>
			<textarea name="comment" rows="3" cols="80" maxlength="4000"></textarea><br/>
			<input type = "submit" value = "<fmt:message key="command.add"/>"/>
		</form>
	</body>
</html>
