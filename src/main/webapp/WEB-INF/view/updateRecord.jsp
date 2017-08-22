<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="updateRecord" method="post">
    <table>
        <input type="hidden" name="columnCount" value="${columnCount}"/>
        <input type="hidden" name="tableName" value="${tableName}"/>
        <input type="hidden" name="keyValue" value="${keyValue}"/>

        <c:forEach begin="1" end="${columnCount - 1}" varStatus="loop">
            <tr>
                <td>
                    Column ${loop.count} value
                </td>
                <td>
                    <input name="columnValue${loop.count}"/>
                </td>
            </tr>
        </c:forEach>

        <tr>
            <td></td>
            <td>
                <input type="submit" value="update"/>
            </td>
        </tr>
    </table>
</form>
<%@include file="footer.jsp" %>
</body>
</html>