<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<table border="1">
    <caption>Tables</caption>
    <tr>
        <th>Table Name</th>
        <th colspan="2">Action</th>
    </tr>
    <c:forEach items="${tables}" var="name">
        <tr>
            <td><a href="find?table=${name}">${name}</a></td>
            <td>
                <form action="clear" method="post">
                    <input type="hidden" name="table" value=${name}>
                    <input type="submit" value="Clear">
                </form>
                <%--<a href="clear?table=${name}">clear</a></td>--%>
            </td>
            <td>
                <form action="deleteTable" method="post">
                    <input type="hidden" name="table" value="${name}">
                    <input type="submit" value="Delete">
                </form>
                <%--<a href="deleteTable?table=${name}">delete</a></td>--%>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>