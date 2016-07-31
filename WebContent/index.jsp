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
		<c:choose>
			<c:when test="${language=='ru'}">
				<a href="SetLanguage?language=en">EN</a> RU
			</c:when>
			<c:otherwise>
				EN <a href="SetLanguage?language=ru">RU</a>
			</c:otherwise>
		</c:choose>
		<h1><fmt:message key="app.name"/></h1>
		<c:choose>
			<c:when test="${empty user}">
				<a href="login.jsp"><fmt:message key="command.login"/></a>
			</c:when>
			<c:otherwise>
				<h2>${user.login}</h2>
				<a href="LogOut"><fmt:message key="command.logout"/></a><br/>
				<br/><fmt:message key="label.subjects_for_testing"/>:
				<ol style="list-style-type: none">
					<c:forEach var="subject" items="${subjects}">
						<li>
							<a href="DisplayTests?subjectId=${subject.id}">${subject.name}</a>
						</li>
					</c:forEach>
				</ol>
			</c:otherwise>
		</c:choose>
	</body>
</html>