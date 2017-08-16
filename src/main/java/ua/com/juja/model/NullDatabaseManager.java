package ua.com.juja.model;

import java.util.*;

public class NullDatabaseManager implements DatabaseManager {
    @Override
    public Set<String> getTableNames() {
        return new HashSet<>();
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        return new LinkedList<>();
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        //NOP
    }

    @Override
    public void clear(String tableName) {
        //NOP
    }

    @Override
    public void create(String tableName, Map<String, Object> columnData) {
        //NOP
    }

    @Override
    public void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData) {
        //NOP
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new HashSet<>();
    }

    @Override
    public void createDatabase(String databaseName) {
        //NOP
    }

    @Override
    public Set<String> databasesList() {
        return new HashSet<>();
    }

    @Override
    public void deleteTable(String tableName) {
        //NOP
    }

    @Override
    public void delete(String tableName, String keyValue) {
        //NOP
    }

    @Override
    public void deleteDatabase(String databaseName) {
        //NOP
    }

    @Override
    public void disconnectFromDB() {
        //NOP
    }

    @Override
    public void createTable(String tableName, List<String> columnParameters) {
        //NOP
    }

    @Override
    public String getDatabaseName() {
        return "";
    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
