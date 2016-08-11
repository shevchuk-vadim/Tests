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
		<style type="text/css">
			label {
				display: inline-block;
				width: 110px;
			}
 		</style>
	</head>
	<body>
		<nav>
			<a href="index.jsp"><fmt:message key="command.home"/></a>
		</nav>

		<header>
			<h1><fmt:message key="app.name"/></h1>
		</header>
	
		<main>
			<form name="registrationForm" method="post" action="Register">
				<p>
					<label for="loginId"><fmt:message key="label.login"/>:</label>
					<input id="loginId" type="text" name="login" required maxlength="50" value="${login}"/>
				</p>
				<p>
					<label for="passwordId"><fmt:message key="label.password"/>:</label>
					<input id="passwordId" type="password" name="password" required maxlength="50"/>
				</p>
				<p>
					<label for="verifyPasswordId"><fmt:message key="label.verify_password"/>:</label>
					<input id="verifyPasswordId" type="password" name="verifyPassword" required maxlength="50"/>
				</p>
				<p>
					<label for="tutorId"><fmt:message key="label.tutor"/>:</label>
					<input id="tutorId" type="checkbox" name="isTutor" <c:if test="${isTutor}">checked</c:if>/>
				</p>
				<c:if test="${not empty errorMessage}">
					<p style="color: red"><fmt:message key="${errorMessage}"/></p>
				</c:if>
				<input type="submit" value="<fmt:message key="command.register"/>"/>
			</form>
		</main>
	</body>
</html>
