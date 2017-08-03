package ua.com.juja.service;

import ua.com.juja.model.DataSet;
import ua.com.juja.model.DataSetImpl;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.model.PostgresManager;

import java.util.*;

public class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect", "find", "clear", "createdbform", "createtableform",
                "databaselist", "deletedbform", "deletetableform", "insertentryform");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = new PostgresManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<DataSet> tableData = manager.getTableData(tableName);
        result.add(columns);
        tableData.forEach(dataSet -> {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            columns.forEach(column -> row.add(dataSet.get(column).toString()));
        });
        return result;
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) {
        manager.clear(tableName);
    }

    @Override
    public void createdatabase(DatabaseManager manager, String databaseName) {
        manager.createDatabase(databaseName);
    }

    @Override
    public void createtable(DatabaseManager manager, String sql) {
        manager.createTable(sql);
    }

    @Override
    public Set<String> databaselist(DatabaseManager manager) {
        Set<String> databases = manager.databasesList();
        databases.forEach(database -> database.toString());
        return databases;
    }

    @Override
    public void deletedatabase(DatabaseManager manager, String dbname) {
        manager.deleteDatabase(dbname);
    }

    @Override
    public void deletetable(DatabaseManager manager, String tableName) {
        manager.deleteTable(tableName);
    }

    @Override
    public void insertentry(DatabaseManager manager, String tableName, String columnNames, String values) {
        String[] column = columnNames.split(" ");
        String[] value = values.split(" ");

        DataSet dataset = new DataSetImpl();
        for (int i = 0; i < column.length; i++) {
            for (int j = 0; j < value.length; j++) {
                dataset.put(column[i], value[i]);
                break;
            }
        }
        manager.create(tableName, dataset);
    }
}
