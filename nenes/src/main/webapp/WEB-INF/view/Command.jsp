<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<title>Command</title>
</head>
<body>
	<p>에이전트에게 메시지를 송신합니다.</p>
	<p></p>
	<form:form action="command" commandName="message">
		<label>에이전트 선택: <form:checkboxes path="agent" items="${agents}" /> </label> <br /> 
		<label>메시지: <form:input path="msg" /></label> 
		<input type="submit" value="전송" />
	</form:form>
</body>
</html>