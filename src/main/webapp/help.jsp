<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
+----------------------------COMMANDS------------------------------<br>

 connect database username password<br>
    -> To get to the database, with which it is necessary to work<br>
 tables<br>
    -> To get a list of all database tables<br>
 databases<br>
    -> To get a list of all databases<br>
 find tableName<br>
    -> To retrieve table contents 'tableName'<br>
 clear tableName<br>
    -> To clear the entire table with the name 'tableName'<br>
 insertEntry tableName column1 value1 column2 value2...columnN valueN<br>
    -> To create an entry in the table named 'tableName'<br>
 updateEntry tableNameID<br>
    -> update the entry in the table 'tableName' using the ID<br>
 createDatabase databaseName<br>
    -> Create new database named 'databaseName'<br>
 create TabletableName<br>
    -> Create new table named 'tableName'<br>
 deleteDatabase databaseName<br>
    -> Delete database named 'databaseName'<br>
 deleteTable tableName<br>
    -> Delete table named 'tableName'<br>
 disconnect<br>
    -> disconnect from current database<br>
 exit<br>
    -> To terminate the application<br>
 help<br>
    -> To display this list on the screen<br>
</body>
</html>