package ua.com.juja.model;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.*;

@Component
@Scope(value = "prototype")
public class PostgresManager implements DatabaseManager {

    private Connection connection;
    private String database;
    private String userName;
    private JdbcTemplate template;

    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String PROTOCOL = "jdbc:postgresql:";
    private static final String CONNECTION_EXCEPTION = "Can't get connection for model :%s user:%s";
    private static final String TABLE_LIST = "SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema = 'public'";
    private static final String TABLE_FIND = "SELECT * FROM public.%s";
    private static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String CLEAR_TABLE = "DELETE from public.%s";
    private static final String UPDATE = "UPDATE public.%s SET %s WHERE id = ?";
    private static final String SELECT = "SELECT * FROM information_schema.columns " +
            "WHERE table_schema = 'public'  AND table_name = '%s'";
    private static final String DATABASE_LIST = "SELECT datname FROM pg_database WHERE datistemplate = false;";
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS %s";
    private static final String CREATE_DATABASE = "CREATE DATABASE %s ENCODING 'UTF8'";
    private static final String DELETE_RECORD = "DELETE FROM %s WHERE id = '%s'";
    private static final String DELETE_DATABASE = "DROP DATABASE %s";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s(id INT NOT NULL PRIMARY KEY, %s)";
    private static final int DATABASE_NAME_INDEX = 1;


    @Override
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(template.query(TABLE_LIST,
                (resultSet, rowNum) -> resultSet.getString("table_name")));
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        return template.query(String.format(TABLE_FIND, tableName),
                (resultSet, rowNum) -> {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    DataSet input = new DataSetImpl();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        input.put(metaData.getColumnName(i),
                                resultSet.getString(i));
                    }
                    return input;
                });
    }

    @Override
    public void connect(String database, String userName, String password) {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DatabaseManagerException("Please add jdbc jar to project.", e);
        }
        try {
            if (!StringUtils.isEmpty(connection)) {
                connection.close();
            }
            String url = String.format("%s//%s:%s/", PROTOCOL, HOST, PORT);
            connection = DriverManager.getConnection(
                    url + database, userName, password);
            this.database = database;
            this.userName = userName;
            template = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
        } catch (SQLException e) {
            connection = null;
            template = null;
            throw new DatabaseManagerException(
                    String.format(CONNECTION_EXCEPTION, database, userName), e);
        }
    }

    @Override
    public void clearTable(String tableName) {
        template.execute(String.format(CLEAR_TABLE, tableName));
    }

    @Override
    public void insertRecord(String tableName, Map<String, Object> columnData) {
        String columnsNames = StringUtils.collectionToDelimitedString(
                columnData.keySet(), ",");
        String values = StringUtils.collectionToDelimitedString(
                columnData.values(), ",", "'", "'");

        template.update(String.format(INSERT, tableName, columnsNames, values));
    }

    @Override
    public void updateRecord(String tableName, Integer keyValue, Map<String, Object> columnData) {
        String tableNames = StringUtils.collectionToDelimitedString(
                columnData.keySet(), ",", "", " = ?");
        List<Object> objects = new LinkedList<>(columnData.values());
        objects.add(keyValue);
        String sql = (String.format(UPDATE, tableName, tableNames));

        template.update(sql, objects.toArray());
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<>(template.query(String.format(SELECT, tableName),
                (resultSet, rowNum) -> resultSet.getString("column_name")));
    }


    @Override
    public void createDatabase(String databaseName) {
        template.execute(String.format(CREATE_DATABASE, databaseName));
    }

    @Override
    public Set<String> databasesList() {
        return new LinkedHashSet<>(template.query(DATABASE_LIST,
                (resultSet, rowNum) -> resultSet.getString(DATABASE_NAME_INDEX)));
    }

    @Override
    public void deleteTable(String tableName) {
        template.execute(String.format(DELETE_TABLE, tableName));
    }

    @Override
    public void deleteRecord(String tableName, String keyValue) {
        template.execute(String.format(DELETE_RECORD, tableName, keyValue));
    }

    @Override
    public void deleteDatabase(String databaseName) {
        template.execute(String.format(DELETE_DATABASE, databaseName));
    }

    @Override
    public void createTable(String tableName, List<String> columnParameters) {
        String parameters = StringUtils.collectionToDelimitedString(
                columnParameters, ",", "", " varchar(50)");
        template.update(String.format(CREATE_TABLE, tableName, parameters));
    }

    @Override
    public String getDatabaseName() {
        return database;
    }

    @Override
    public String getUserName() {
        return userName;
    }
}