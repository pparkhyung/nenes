<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>nene main</title>
</head>
<body>
	<p>환영합니다.</p>
	<ul>
		<li><a href="<c:url value="/command" />">메시지 송신 모드 (Telnet)</a></li>
		<li><a href="<c:url value="/file" />">파일 송신 모드</a></li>
		<li><a href="<c:url value="/operation" />">작업 송신 모드</a></li>
	</ul>
</body>
</html>
