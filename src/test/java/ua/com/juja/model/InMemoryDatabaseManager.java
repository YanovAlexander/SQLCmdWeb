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
        return get(tableName);
    }

    @Override
    public void connect(String database, String userName, String password) {
        // do nothing
    }

    @Override
    public void clearTable(String tableName) {
        get(tableName).clear();
    }

    @Override
    public void insertRecord(String tableName, Map<String, Object> columnData) {
        DataSet data = new DataSetImpl();
        for (Map.Entry<String, Object> pair : columnData.entrySet()) {
            data.put(pair.getKey(), pair.getValue());
        }
        get(tableName).add(data);
    }

    private List<DataSet> getWithDataSet(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }


    @Override
    public void updateRecord(String tableName, Integer keyValue, Map<String, Object> columnData) {

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
    public void deleteRecord(String tableName, String keyValue) {

    }

    @Override
    public void deleteDatabase(String databaseName) {

    }

    @Override
    public void createTable(String tableName, List<String> columnParameters) {

    }

    @Override
    public String getDatabaseName() {
        return null;
    }

    @Override
    public String getUserName() {
        return "";
    }
}
