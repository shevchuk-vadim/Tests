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
			@import "/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/Tests/WebContent/style.css";
 		</style>
	</head>
	<body>
		<nav style="background-color: lightgrey; padding: 5px 0 5px">
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
			${pageContext.request.contextPath}<br/>
			<c:url value="/WebContents/style.css"/>
			<div style="width: 50%; text-align: center">
				<h1><fmt:message key="app.name"/></h1>
			</div>
		</header>

		<main style="background-color: lightblue; padding: 1px 0 1px">
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