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
		<nav style="background-color: lightgrey; padding: 5px 0 5px">
			<a href="index.jsp"><fmt:message key="command.home"/></a>
		</nav>	
		
		<header style="background-color: lightgreen; padding: 1px 0 1px">
			<div style="width: 50%; text-align: center">
				<h2>
					<fmt:message key="label.tests"/>
					<fmt:message key="label.on_subject"/>
					${subject.name}
				</h2>
			</div>
		</header>
		
		<main style="background-color: lightblue; padding: 1px 0 1px">
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