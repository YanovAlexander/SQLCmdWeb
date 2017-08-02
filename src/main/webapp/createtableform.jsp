<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="createtableform" method="post">
    <table>
        <tr>
            <td>Table name</td>
            <td><input  name="tablename"/></td>
        </tr>
        <tr>
            <td>Column 1(Primary key)</td>
            <td><input  name="column1"/></td>
        </tr>
        <tr>
            <td>Column 2</td>
            <td><input name="column2"/></td>
        </tr>
        <tr>
            <td>Column 3</td>
            <td><input name="column3"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="createtableform"/></td>
        </tr>
    </table>
</form>
</body>
</html>