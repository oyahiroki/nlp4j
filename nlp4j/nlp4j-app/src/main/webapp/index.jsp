<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="ja">
<head>
<meta charset="UTF-8">
</head>
<body>
<h2>Hello World!</h2>
<h2><%= new java.util.Date() %></h2>
<ul>
<li><a href="helloservlet1">helloservlet1</a></li>
<li><a href="helloservlet2?param1=test">helloservlet2?param=test</a> (Response is HTML)</li>
<li><a href="helloservlet3?param1=test">helloservlet3?param=test</a> (Response is JSON)</li>
<li><a href="helloservlet3?param1=%E6%97%A5%E6%9C%AC%E8%AA%9E">helloservlet3?param1=%E6%97%A5%E6%9C%AC%E8%AA%9E</a></li>
<li><a href="helloservlet4">helloservlet4 for download</a></li>
</ul>
<ul>
<li><a href="hello-bootstrap5.jsp">hello-bootstrap5.jsp</a></li>
</ul>
</body>
</html>
