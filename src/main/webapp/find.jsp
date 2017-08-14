<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            <td><a href="record?record=${row[0]}">clear</a></td>
        </c:if>
        </c:forEach>
    </tr>
</table>
<%@include file="footer.jsp" %>
</body>
</html>