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
				width: 75px;
			}
 		</style>
	</head>
	<body>
		<nav>
			<div style="width: 10%; float: left">
				<a href="index.jsp"><fmt:message key="command.home"/></a>
			</div>
			
			<div style="width: 50%; text-align: right">
				<a href="registration.jsp"><fmt:message key="command.register"/></a>
			</div>
		</nav>

		<header>
			<h1><fmt:message key="app.name"/></h1>
		</header>

		<main>
			<form name="loginForm" method="post" action="LogIn">
				<p>
					<label for="loginId"><fmt:message key="label.login"/>:</label>
					<input id="loginId" type="text" name="login" required maxlength="50" value="${login}"/>
				</p>
				<p>
					<label for="passwordId"><fmt:message key="label.password"/>:</label>
					<input id="passwordId" type="password" name="password" required maxlength="50"/>
				</p>
				<c:if test="${not empty errorMessage}">
					<p style="color: red"><fmt:message key="${errorMessage}"/></p>
				</c:if>
				<input type="submit" value="<fmt:message key="command.login"/>"/>
			</form>
		</main>
	</body>
</html>