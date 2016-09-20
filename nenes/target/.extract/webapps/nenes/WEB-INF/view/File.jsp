<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>File</title>
</head>
<body>
	<p>에이전트에게 파일를 송신합니다.</p>
	<p></p>
	<form:form action="file" commandName="fileMessage">
		<label>에이전트 선택: <form:checkboxes path="agent" items="${agents}" /> </label> <br /> 
		<label>파일: <form:input path="fileName" value="d:\nene4data.zip"/></label> 
		<input type="submit" value="전송" />
	</form:form>
		<li><a href="<c:url value="/operation" />">작업 송신 모드</a></li>
</body>
</html>