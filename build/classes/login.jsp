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
		<a href="registration.jsp"><fmt:message key="command.register"/></a>
		<form name="loginForm" method="post" action="LogIn">
			<br/><fmt:message key="label.login"/>: <input type="text" name="login" required maxlength="50" value="${login}"/><br/>
			<br/><fmt:message key="label.password"/>: <input type="password" name="password" required maxlength="50"/><br/>
			<c:if test="${not empty errorMessage}">
				<br/><fmt:message key="${errorMessage}"/><br/>
			</c:if>
			<br/><input type="submit" value="<fmt:message key="command.login"/>"/>
		</form>
	</body>
</html>
