<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>

<table border="1">
    <caption>
        Databases
    </caption>
    <tr>
        <th>
            Database Name
        </th>
        <th>
            Action
        </th>
    </tr>
    <c:forEach items="${databases}" var="database">
        <tr>
            <td>
                    ${database}
            </td>
            <td>
                <form action="delete-database" method="post">
                    <input type="hidden" name="database" value="${database}">
                    <input type="submit" value="Delete">
                </form>
        </tr>
    </c:forEach>
</table>
<br>
<%@include file="footer.jsp" %>
</body>
</html>