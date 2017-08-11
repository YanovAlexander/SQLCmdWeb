package ua.com.juja.service;

import org.springframework.stereotype.Component;
import ua.com.juja.model.DataSet;
import ua.com.juja.model.DatabaseManager;

import java.util.*;


@Component
public abstract class ServiceImpl implements Service {

    public abstract DatabaseManager getManager();

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "list");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public List<List<String>> find(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<DataSet> tableData = manager.getTableData(tableName);

        result.add(columns);
        for (DataSet dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns) {
                Object value = dataSet.get(column);
                if (value == null) {
                    throw new IllegalStateException(String.format(
                            "Can't find column %s, but was %s",
                            column, dataSet));
                }
                row.add(value.toString());
            }
        }

        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        return manager.getTableNames();
    }
}
