package ua.com.juja.service;

import ua.com.juja.model.DataSet;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.model.PostgresManager;

import java.util.*;

public class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect", "find", "clear");
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
}
