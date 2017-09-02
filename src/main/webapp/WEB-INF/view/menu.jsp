<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<table border="1">
    <c:forEach items="${items}" var="item">
        <tr>
            <td>
                <b> <a href="${item}">${item}</a></b><br>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
