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
		<style type="text/css">
			table {
				border: thin solid black;
				width: 50%;
			}
			th {
				background: silver;
			}
			td {
				color: blue;
			}
			.col {
				width: 100px;
			}
 		</style>
	</head>
	<body>
		<a href="index.jsp"><fmt:message key="command.home"/></a><br/>	
		<h2><fmt:message key="label.tests"/> <fmt:message key="label.on_subject"/> ${subject.name}</h2>
		<c:if test="${user.tutor}">
			<a href="StartTesting?testId=0"><fmt:message key="command.create_test"/></a><br/><br/>
		</c:if>
		<table border="1">
			<tr>
				<th class="col"><fmt:message key="label.number"/></th>
				<th><fmt:message key="label.author"/></th>
				<th class="col"><fmt:message key="label.passed"/></th>
			</tr>
			<c:forEach var="test" items="${tests}">
				<tr>
					<td><a href="StartTesting?testId=${test.id}"><fmt:message key="label.test"/> № ${test.id}</a></td>
					<td>${test.user.login}</td>
					<td><input type="checkbox" disabled <c:if test="${test.passed}">checked</c:if>/></td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>