<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>SQLCmd</title>
</head>
<body>
<c:forEach items="${list}" var="name">
  <a href="find?table=${name}">${name}</a><br>
</c:forEach>
</body>
</html>