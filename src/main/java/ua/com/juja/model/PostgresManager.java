package ua.com.juja.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static String TABLE_LIST = "SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema = 'public'";
    private static String TABLE_FIND = "SELECT * FROM public.%s";
    private static String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
    private static String UPDATE = "UPDATE %s SET %s = '%s' WHERE %s = '%s'";
    private static String SELECT = "SELECT * FROM information_schema.columns " +
            "WHERE table_schema = 'public'  AND table_name = '%s'";
    private static String DATABASE_LIST = "SELECT datname FROM pg_database WHERE datistemplate = false;";
    private static String DELETE_TABLE = "DROP TABLE IF EXISTS %s";
    private static String CREATE_DATABASE = "CREATE DATABASE %s ENCODING 'UTF8'";
    private static String DELETE_RECORD = "DELETE FROM %s WHERE %s = '%s'";
    private static String DELETE_DATABASE = "DROP DATABASE %s";
    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s(%s INT NOT NULL PRIMARY KEY %s)";



    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(TABLE_LIST)) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<String> getTableData(String tableName) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(TABLE_FIND, tableName))) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<String> tableData = new ArrayList<>();
            tableData.add(String.valueOf(metaData.getColumnCount()));
            for (int indexColumn = 1; indexColumn <= metaData.getColumnCount(); indexColumn++) {
                tableData.add(resultSet.getMetaData().getColumnName(indexColumn));
            }

            while (resultSet.next()) {
                for (int indexData = 1; indexData <= metaData.getColumnCount(); indexData++) {
                    if (resultSet.getString(indexData) == null) {
                        tableData.add("");
                    } else {
                        tableData.add(resultSet.getString(indexData));
                    }
                }
            }
            return tableData;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        if (userName != null && password != null) {
            this.userName = userName;
            this.password = password;
        }
        if (!"".equals(databaseName)) {
            isConnected = true;
        }
        this.database = databaseName;
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
            connection = DriverManager.getConnection(url + databaseName,
                    userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new DatabaseManagerException(
                    String.format("Can't get connection for model :%s user:%s", databaseName, userName), e);
        }
    }

    @Override
    public void clear(String tableName) {
        executeUpdateQuery("DELETE from public." + tableName);
    }

    private void executeUpdateQuery(String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseManagerException("Wrong command, please try again. ", e.getCause());
        }
    }

    @Override
    public void create(String tableName, Map<String, Object> columnData) {
        try (Statement statement = connection.createStatement()) {

            String columnsNames = getNamesFormatted(columnData);
            String values = getValuesFormatted(columnData);

            statement.executeUpdate(String.format(INSERT, tableName, columnsNames, values ));
        } catch (SQLException e) {
            throw new DatabaseManagerException("Cant insert to " + tableName, e.getCause());
        }
    }

    @Override
    public void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData) {
        try (Statement statement = connection.createStatement()) {
            for (Map.Entry<String, Object> pair : columnData.entrySet()) {
                statement.executeUpdate(String.format(UPDATE,
                        tableName, pair.getKey(), pair.getValue(), keyName, keyValue ));
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException("Cant update , please try again (cant update Primary Key field)", e.getCause());
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> tables = new LinkedHashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format(SELECT, tableName))) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("column_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createDatabase(String databaseName) {
        executeUpdateQuery(String.format(CREATE_DATABASE, databaseName));
    }

    @Override
    public Set<String> databasesList() {
        Set<String> result = new LinkedHashSet<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(DATABASE_LIST)) {
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteTable(String tableName) {
        executeUpdateQuery(String.format(DELETE_TABLE, tableName));
    }

    @Override
    public void delete(String tableName, String keyName, String keyValue) {
        executeUpdateQuery(String.format(DELETE_RECORD, tableName, keyName, keyValue));
    }

    @Override
    public void deleteDatabase(String databaseName) {
        executeUpdateQuery(String.format(DELETE_DATABASE, databaseName));
    }

    @Override
    public void createTable(String tableName, String keyName, Map<String, Object> columnParameters) {
        executeUpdateQuery(String.format(CREATE_TABLE
                ,tableName, keyName, getParameters(columnParameters) ));
    }

    private String getParameters(Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
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

    private String getValuesFormatted(Map<String, Object> columnData) {
        String values = "";
        for (Map.Entry<String, Object> pair : columnData.entrySet()) {
            values += "'" + pair.getValue() + "', ";
        }
        return values.substring(0, values.length() - 2);
    }

    @Override
    public String getDatabaseName() {
        return database;
    }

    private String getNamesFormatted(Map<String, Object> columnData) {
        String keys = "";
        for (Map.Entry<String, Object> pair : columnData.entrySet()) {
            keys += pair.getKey() + ", ";
        }
        return keys.substring(0, keys.length() - 2);
    }
}