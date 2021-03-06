<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="pagecontent"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8"></meta>
		<title><fmt:message key="app.name"/></title>
		<style type="text/css">
			<jsp:include page="style.css"/>
 		</style>
	</head>
	<body>
		<nav>
			<div style="width: 10%; float: left">
				<c:choose>
					<c:when test="${language=='ru'}">
						<a href="SetLanguage?language=en">EN</a> RU
					</c:when>
					<c:otherwise>
						EN <a href="SetLanguage?language=ru">RU</a>
					</c:otherwise>
				</c:choose>
			</div>
			
			<div style="width: 50%; text-align: right">
				<c:if test="${empty user}" var="emptyUser">
					<a href="login.jsp"><fmt:message key="command.login"/></a>
				</c:if>
				<c:if test="${not emptyUser}">
					<b style="font-size: large">${user.login}</b>
					<a href="LogOut"><fmt:message key="command.logout"/></a>
				</c:if>
			</div>
		</nav>

		<header>
			<h1><fmt:message key="app.name"/></h1>
		</header>

		<main>
			<c:if test="${not emptyUser}">
				<h3><fmt:message key="label.subjects_for_testing"/>:</h3>
				<ol style="list-style-type: circle">
					<c:forEach var="subject" items="${subjects}">
						<li>
							<a href="DisplayTests?subjectId=${subject.id}">${subject.name}</a>
						</li>
					</c:forEach>
				</ol>
			</c:if>
		</main>
	</body>
</html>