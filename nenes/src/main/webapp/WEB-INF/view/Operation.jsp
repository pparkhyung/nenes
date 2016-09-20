<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>Operation</title>
</head>
<body>
	<p>에이전트에게 작업을 요청합니다.</p>
	<p></p>
	<form:form action="operation" commandName="message">
		<label>에이전트 선택: <form:checkboxes path="agent" items="${agents}" /> </label> <br /> 
		<label>작업내용: <form:input path="msg" /></label> 
		<input type="submit" value="전송" />
	</form:form>
		<li><a href="<c:url value="/file" />">파일 송신 모드</a></li>
</body>
</html>