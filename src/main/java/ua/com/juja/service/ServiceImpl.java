package ua.com.juja.service;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.model.PostgresManager;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {

    private DatabaseManager manager;

    public ServiceImpl(){
        manager = new PostgresManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        manager.connect(databaseName, userName, password);
    }
}
