<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>

<table border="1">
    <c:forEach items="${table}" var="row">
    <tr>
        <c:forEach items="${row}" var="element">
            <td>
                    ${element}
            </td>
        </c:forEach>
        <c:if test="${row != table[0]}">
            <td>
                <form action="deleteRecord" method="post">
                    <input type="hidden" name="record" value=${row[0]}>
                    <input type="hidden" name="tableName" value=${tableName}>
                    <input type="submit" value="Clear"/>
                </form>
            </td>
            <td>
                <form action="updateRecord">
                    <input type="hidden" name="record" value="${row[0]} ">
                    <input type="hidden" name="table" value="${table[0]}">
                    <input type="hidden" name="tableName" value=${tableName}>
                    <input type="submit" value="Update">
                </form>
            </td>
        </c:if>
        <c:if test="${row == table[0]}">
            <th colspan="2">
                Action
            </th>
        </c:if>
        </c:forEach>
    </tr>
</table>
<br>
<form action="insertRecord">
    <input type="hidden" name="table" value="${table[0]}">
    <input type="hidden" name="tableName" value="${tableName}">
        <input type="submit" value="Insert">
</form>
<%@include file="footer.jsp" %>
</body>
</html>