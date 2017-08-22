package ua.com.juja.service;

import ua.com.juja.model.DatabaseManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Service {

    DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException;

    void clear(DatabaseManager manager, String tableName);

    void createDatabase(DatabaseManager manager, String databaseName);

    void deleteDatabase(DatabaseManager manager, String databaseName);

    void update(DatabaseManager manager, String tableName, Integer keyValue, Map<String, Object> data);

    void deleteTable(DatabaseManager manager, String tableName);


    List<List<String>> find(DatabaseManager manager, String tableName);

    Set<String> tables(DatabaseManager manager);

    void deleteRecord(DatabaseManager manager, String tableName, String keyValue);

    void createTable(DatabaseManager manager, String tableName, List<String> columnParameters);

    Set<String> databases(DatabaseManager manager);


    void insert(DatabaseManager databaseManager, String tableName, Map<String, Object> data);
}
