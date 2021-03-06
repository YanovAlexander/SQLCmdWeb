<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="insert-record" method="post">
    <table>
        <input type="hidden" name="columnCount" value="${columnCount}"/>
        <input type="hidden" name="tableName" value="${tableName}"/>
        <tr>
            <c:forEach begin="1" end="${columnCount}" varStatus="loop">
            <td>
                Column value ${loop.count}
            </td>
            <td>
                <label>
                    <input name="columnValue${loop.count - 1}"/>
                </label>
            </td>
        </tr>
        </c:forEach>
        <tr>
            <td>
                <input type="submit" value="Insert"/>
                <%@include file="footer.jsp" %>
            </td>
        </tr>
    </table>
</form>
</body>
</html>