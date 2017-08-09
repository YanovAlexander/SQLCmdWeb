<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>sqlcmd</title>
</head>
<body>
<form action="create" method="post">
    <table>
        <input type="hidden" name="columnCount" value="${columnCount}"/>
        <input type="hidden" name="tableName" value="${tableName}"/>
        <tr>
            <c:forEach begin="1" end="${columnCount}" varStatus="loop">
        <tr>
            <td>Column name ${loop.count}</td>
            <td><label>
                <input name="columnName${loop.count}"/>
            </label></td>

            <td>Column value ${loop.count}</td>
            <td><label>
                <input name="columnValue${loop.count}"/>
            </label></td>
        </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td><input type="submit" value="create"/></td>
        </tr>
    </table>
</form>
</body>
</html>