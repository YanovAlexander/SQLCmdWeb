package ua.com.juja.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {
    Set<String> getTableNames();

    List<DataSet> getTableData(String tableName);

    void connect(String databaseName, String userName, String password);

    void clear(String tableName);

    void create(String tableName, Map<String, Object> columnData);

    void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData);

    Set<String> getTableColumns(String tableName);

    void createDatabase(String databaseName);

    Set<String > databasesList();

    void deleteTable(String tableName);

    void delete(String tableName, String keyName, String keyValue);

    void deleteDatabase(String databaseName);

    void disconnectFromDB();

    void createTable(String tableName, String keyName, Map<String, Object> columnParameters);

    String getDatabaseName();

    boolean isConnected();
}
