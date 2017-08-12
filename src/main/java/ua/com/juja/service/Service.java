package ua.com.juja.service;

import ua.com.juja.model.DatabaseManager;

import java.util.List;
import java.util.Set;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException;

    void clear(DatabaseManager manager, String tableName);

    void createdatabase(DatabaseManager manager, String databaseName);

    void deletedatabase(DatabaseManager manager, String dbname);

    void update();

    void createDatabase();

    void deleteDatabase();

    void create();

    void deleteTable(DatabaseManager manager, String tableName);


    List<List<String>> find(DatabaseManager manager, String tableName);

    Set<String> tables(DatabaseManager manager);

    void deleteRecord(DatabaseManager manager, String tableName, String keyName, String keyValue);
}
