<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="deletetableform" method="post">
    <table>
        <tr>
            <td>Database name</td>
            <td><input  name="tablename"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="delete"/></td>
        </tr>
    </table>
</form>
<%@include file="footer.jsp" %>
</body>
</html>