package ua.com.juja.model;

import java.util.*;

public class InMemoryDatabaseManager implements DatabaseManager {

    private Map<String, List<DataSet>> tables = new LinkedHashMap<>();

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public List<DataSet> getTableData(String tableName) {
        return null;
    }

    @Override
    public void connect(String database, String userName, String password) {
        // do nothing
    }

    @Override
    public void clear(String tableName) {
        get(tableName).clear();
    }

    @Override
    public void create(String tableName, Map<String, Object> columnData) {
        //do nothing
    }

    private List<DataSet> getWithDataSet(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }


    @Override
    public void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData) {

    }

    private List<DataSet> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<>());
        }
        return tables.get(tableName);
    }



    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<>(Arrays.asList("name", "password", "id"));
    }

    @Override
    public void createDatabase(String databaseName) {

    }

    @Override
    public Set<String> databasesList() {
        return null;
    }

    @Override
    public void deleteTable(String tableName) {

    }

    @Override
    public void delete(String tableName, String keyName, String keyValue) {

    }

    @Override
    public void deleteDatabase(String databaseName) {

    }

    @Override
    public void disconnectFromDB() {

    }

    @Override
    public void createTable(String tableName, String keyName, Map<String, Object> columnParameters) {

    }

    @Override
    public String getDatabaseName() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
