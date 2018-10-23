<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Hello!</h1>
<a href="${vk_url}?client_id=${client_id }&redirect_uri=${redirect_uri }&response_type=${response_type }">Enter from vk</a><br>
 <p>Click <a th:href="@{/login}">here</a> to login directly.</p>
</body>
</html>