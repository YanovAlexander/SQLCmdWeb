package ua.com.juja.service;

import ua.com.juja.model.DatabaseManager;

import java.util.List;
import java.util.Set;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> find(DatabaseManager manager, String tableName);

    void clear(DatabaseManager manager, String tableName);

    void createdatabase(DatabaseManager manager, String databaseName);

    void createtable(DatabaseManager manager, String sql);

    Set<String> databaselist(DatabaseManager manager);

    void deletedatabase(DatabaseManager manager, String dbname);

    void deletetable(DatabaseManager manager, String dbname);

    void insertentry(DatabaseManager manager, String tableName, String columnNames, String values);
}
