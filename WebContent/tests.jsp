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
			table {
				border-collapse: collapse;
				width: 50%;
			}
			th {
				border: thin solid black;
				background: lightgrey;
			}
			td {
				border: thin solid black;
				background: lightgreen;
			}
			.col {
				width: 100px;
				text-align: center;
			}
 		</style>
	</head>
	<body>
		<nav>
			<a href="index.jsp"><fmt:message key="command.home"/></a>
		</nav>	
		
		<header>
			<h2>
				<fmt:message key="label.tests"/>
				<fmt:message key="label.on_subject"/>
				${subject.name}
			</h2>
		</header>
		
		<main>
			<c:if test="${user.tutor}">
				<p>
					<a href="StartTesting?testId=0"><fmt:message key="command.create_test"/></a>
				</p>
			</c:if>
			<table>
				<tr>
					<th class="col"><fmt:message key="label.number"/></th>
					<th><fmt:message key="label.author"/></th>
					<th class="col"><fmt:message key="label.passed"/></th>
				</tr>
				<c:forEach var="test" items="${tests}">
					<tr>
						<td class="col"><a href="StartTesting?testId=${test.id}"><fmt:message key="label.test"/> № ${test.number}</a></td>
						<td>${test.user.login}</td>
						<td class="col"><c:if test="${test.passed}"><b>V</b></c:if></td>
					</tr>
				</c:forEach>
			</table>
		</main>
	</body>
</html>