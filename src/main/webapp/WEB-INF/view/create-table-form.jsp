<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<form action="create-table-form" method="post">
    <table>
        <tr>
            <td>
                Table name
            </td>
            <td>
                <label>
                    <input name="tableName"/>
                </label>
            </td>
        </tr>

        <tr>
            <td>
                Column count
            </td>
            <td>
                <label>
                    <input type="number" name="columnCount"/>
                </label>
            </td>
        </tr>

        <tr>
            <td></td>
            <td>
                <input type="submit" value="OK"/>
            </td>
        </tr>
    </table>
    <%@include file="footer.jsp" %>
</body>
</html>