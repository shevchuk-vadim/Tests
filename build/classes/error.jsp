<%@ page isErrorPage="true" contentType="text/html; charset=utf-8" %>
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
		<a href="index.jsp"><fmt:message key="command.home"/></a><br/><br/>
		<c:choose>
			<c:when test="${pageContext.errorData.statusCode==500}">
				<fmt:message key="error.processing_request"/>
			</c:when>
			<c:when test="${empty errorMessage}">
				<fmt:message key="error.incorrect_request"/>
			</c:when>
			<c:otherwise>
				<fmt:message key="${errorMessage}"/>
			</c:otherwise>
		</c:choose>
	</body>
</html>