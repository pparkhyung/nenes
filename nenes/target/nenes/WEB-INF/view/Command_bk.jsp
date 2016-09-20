<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>

<!DOCTYPE html>
<html>
<head>
<title>Command</title>
</head>
<body>
	<p>에이전트에게 메시지를 송신합니다.</p>
	<p></p>

	<%-- 	<p>
		에이전트를 선택하세요<br />
		<c:forEach var="agent" items="${agents}" varStatus="status">
			<label><input type="radio" name="responses[${status.index}]"
				value="${agent}"> ${agent}</label>
		</c:forEach>
	</p> --%>


	<form:form action="command" commandName="message">
		<%-- <form:select path="agent" items="${agents}" /> --%>
		<%-- <label>에이전트 선택: 
		<form:checkbox path="agent" value="에이전트1" items=" ${agents}" /></label> --%>
		
		<label>에이전트 선택: 
<%-- 		<form:checkbox path="agent" value="에이전트1" label="에이전트1" />
		<form:checkbox path="agent" value="에이전트2" label="에이전트2" />
		<form:checkbox path="agent" value="모두" label="에이전트ALL" /> --%>
		
		<form:checkboxes path="agent" items="${agents}"/>
		<%-- <form:radiobuttons path="agent" items=" ${agents}" /> --%>
		<br />
		
		<label>메시지: <form:input path="msg" /></label>
		<input type="submit" value="전송" />
	</form:form>
</body>
</html>