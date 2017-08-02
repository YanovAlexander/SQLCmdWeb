package ua.com.juja.service;

import ua.com.juja.model.DatabaseManager;

import java.util.List;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> find(DatabaseManager manager, String tableName);

    void clear(DatabaseManager manager, String tableName);

    void createdatabase(DatabaseManager manager, String databaseName);

    void createtable(DatabaseManager manager, String sql);
}
