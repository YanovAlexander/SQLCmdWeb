package ua.com.juja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ua.com.juja.model.DataSet;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.model.entity.UserAction;
import ua.com.juja.model.UserActionsRepository;

import java.util.*;


@Component
public abstract class ServiceImpl implements Service {

    public abstract DatabaseManager getManager();

    @Autowired
    private UserActionsRepository userActions;

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        userActions.save(new UserAction(userName, databaseName, "CONNECT"));
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
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                        "FIND(" + tableName + ")"));

        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                        "TABLES"));

        return manager.getTableNames();
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                        "CLEAR(" + tableName + ")"));

        manager.clearTable(tableName);
    }

    @Override
    public void deleteTable(DatabaseManager manager, String tableName) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                        "DELETE(" + tableName + ")"));

        manager.deleteTable(tableName);
    }

    @Override
    public void deleteRecord(DatabaseManager manager, String tableName, String keyValue) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                        "DELETE_RECORD(" + tableName + ")"));

        manager.deleteRecord(tableName, keyValue);
    }

    @Override
    public void update(DatabaseManager manager, String tableName, Integer keyValue, Map<String, Object> data) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                "UPDATE(table name : " + tableName +
                        ",keyValue " + keyValue + ")"));

        manager.updateRecord(tableName, keyValue, data);
    }

    @Override
    public void createTable(DatabaseManager manager, String tableName, List<String> columnParameters) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                "CREATE_TABLE(" + tableName + ")"));

        manager.createTable(tableName, columnParameters);
    }

    @Override
    public void createDatabase(DatabaseManager manager, String databaseName) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                "CREATE_DATABASE(" + databaseName + ")"));
        manager.createDatabase(databaseName);
    }

    @Override
    public Set<String> databases(DatabaseManager manager) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                        "DATABASE_LIST()"));
        return manager.databasesList();
    }

    @Override
    public void deleteDatabase(DatabaseManager manager, String databaseName) {
        userActions.save(
                new UserAction(manager.getUserName(), manager.getDatabaseName(),
                "DELETE_DATABASE(" + databaseName + ")"));
        manager.deleteDatabase(databaseName);
    }

    @Override
    public void insertRecord(DatabaseManager manager, String tableName, Map<String, Object> data) {
        UserAction action = new UserAction(manager.getUserName(), manager.getDatabaseName(),
                "INSERT_RECORD(table name: " + tableName + ")");
        userActions.save(action);
        manager.insertRecord(tableName, data);
    }

    @Override
    public List<UserAction> getAllFor(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("User name cant be null");
        }
        return userActions.findByUserName(userName);
    }
}
