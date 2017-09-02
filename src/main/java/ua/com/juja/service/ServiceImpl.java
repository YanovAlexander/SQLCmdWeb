package ua.com.juja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ua.com.juja.model.dataSet.DataSet;
import ua.com.juja.model.manager.DatabaseManager;
import ua.com.juja.model.entity.UserAction;
import ua.com.juja.model.repository.UserActionRepository;

import java.util.*;


@Component
public abstract class ServiceImpl implements Service {

    private List<String> commands;

    public abstract DatabaseManager getManager();

    @Autowired
    private UserActionRepository userActions;

    @Override
    public List<String> commandList() {
        return commands;
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = getManager();
        manager.connect(databaseName, userName, password);
        String action = "CONNECT";
        userActions.saveAction(databaseName, userName, action);
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
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "FIND(" + tableName + ")");

        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager manager) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "TABLES");

        return manager.getTableNames();
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "CLEAR(" + tableName + ")");

        manager.clearTable(tableName);
    }

    @Override
    public void deleteTable(DatabaseManager manager, String tableName) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "DELETE(" + tableName + ")");

        manager.deleteTable(tableName);
    }

    @Override
    public void deleteRecord(DatabaseManager manager, String tableName, String keyValue) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "DELETE_RECORD(" + tableName + ")");

        manager.deleteRecord(tableName, keyValue);
    }

    @Override
    public void update(DatabaseManager manager, String tableName, Integer keyValue, Map<String, Object> data) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "UPDATE(table name : " + tableName +
                ",keyValue " + keyValue + ")");

        manager.updateRecord(tableName, keyValue, data);
    }

    @Override
    public void createTable(DatabaseManager manager, String tableName, List<String> columnParameters) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "CREATE_TABLE(" + tableName + ")");

        manager.createTable(tableName, columnParameters);
    }

    @Override
    public void createDatabase(DatabaseManager manager, String databaseName) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "CREATE_DATABASE(" + databaseName + ")");
        manager.createDatabase(databaseName);
    }

    @Override
    public Set<String> databases(DatabaseManager manager) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "DATABASE_LIST()");
        return manager.databasesList();
    }

    @Override
    public void deleteDatabase(DatabaseManager manager, String databaseName) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(), "DELETE_DATABASE(" + databaseName + ")");
        manager.deleteDatabase(databaseName);
    }

    @Override
    public void insertRecord(DatabaseManager manager, String tableName, Map<String, Object> data) {
        userActions.saveAction(manager.getDatabaseName(), manager.getUserName(),
                "INSERT_RECORD(table name: " + tableName + ")");
        manager.insertRecord(tableName, data);
    }

    @Override
    public List<UserAction> getAllFor(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("User name cant be null");
        }
        return userActions.findByUserName(userName);
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
