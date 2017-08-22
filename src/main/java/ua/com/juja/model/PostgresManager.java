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
    private String password;
    private boolean isConnected;
    private JdbcTemplate template;

    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static String TABLE_LIST = "SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema = 'public'";
    private static String TABLE_FIND = "SELECT * FROM public.%s";
    private static String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
    private static String CLEAR = "DELETE from public.%s";
    private static String UPDATE = "UPDATE public.%s SET %s WHERE id = ?";
    private static String SELECT = "SELECT * FROM information_schema.columns " +
            "WHERE table_schema = 'public'  AND table_name = '%s'";
    private static String DATABASE_LIST = "SELECT datname FROM pg_database WHERE datistemplate = false;";
    private static String DELETE_TABLE = "DROP TABLE IF EXISTS %s";
    private static String CREATE_DATABASE = "CREATE DATABASE %s ENCODING 'UTF8'";
    private static String DELETE_RECORD = "DELETE FROM %s WHERE id = '%s'";
    private static String DELETE_DATABASE = "DROP DATABASE %s";
    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s(id INT NOT NULL PRIMARY KEY, %s)";


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
                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        input.put(metaData.getColumnName(i + 1),
                                resultSet.getString(i + 1));
                    }
                    return input;
                });
    }

    @Override
    public void connect(String database, String userName, String password) {
        if (userName != null && password != null) {
            this.userName = userName;
            this.password = password;
        }
        if (!"".equals(database)) {
            isConnected = true;
        }
        this.database = database;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseManagerException("Please add JDBC jar to project.", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
            String url = String.format("jdbc:postgresql://%s:%s/", HOST, PORT);
            connection = DriverManager.getConnection(url + database,
                    userName, password);
            template = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
        } catch (SQLException e) {
            connection = null;
            template = null;
            throw new DatabaseManagerException(
                    String.format("Can't get connection for model :%s user:%s", database, userName), e);
        }
    }

    @Override
    public void clear(String tableName) {
        template.execute(String.format(CLEAR, tableName));
    }

    @Override
    public void create(String tableName, Map<String, Object> columnData) {
        String columnsNames = StringUtils.collectionToDelimitedString(
                columnData.keySet(), ",");
        String values = StringUtils.collectionToDelimitedString(
                columnData.values(), ",", "'", "'");

        template.update(String.format(INSERT, tableName, columnsNames, values));
    }
//TODO remove keyName
//TODO finish jdbc template
    @Override
    public void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData) {
        String tableNames = StringUtils.collectionToDelimitedString(
                columnData.keySet(), ",", "", " = ?");
        List<Object> objects = new LinkedList<>(columnData.values());
        objects.add(Integer.parseInt(keyValue));
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
                (resultSet, rowNum) -> resultSet.getString(1)));
    }

    @Override
    public void deleteTable(String tableName) {
        template.execute(String.format(DELETE_TABLE, tableName));
    }

    @Override
    public void delete(String tableName, String keyValue) {
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
    public void disconnectFromDB() {
        isConnected = false;
        connect("", userName, password);
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public String getDatabaseName() {
        return database;
    }

}