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
        <th>
            Table Name
        </th>
        <th colspan="2">
            Action
        </th>
    </tr>
    <c:forEach items="${tables}" var="name">
        <tr>
            <td>
                <a href="find?table=${name}">
                        ${name}
                </a>
            </td>
            <td>
                <form action="clear-table" method="post">
                    <input type="hidden" name="tableName" value=${name}>
                    <input type="submit" value="Clear">
                </form>
            </td>
            <td>
                <form action="delete-table" method="post">
                    <input type="hidden" name="tableName" value="${name}">
                    <input type="submit" value="Delete">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<%@include file="footer.jsp" %>
</body>
</html>