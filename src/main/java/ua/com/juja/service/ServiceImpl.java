package ua.com.juja.service;

import org.springframework.stereotype.Component;
import ua.com.juja.model.DataSet;
import ua.com.juja.model.DatabaseManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Component
public abstract class ServiceImpl implements Service {

    public abstract DatabaseManager getManager();

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException {
        try {
            DatabaseManager manager = getManager();
            manager.connect(databaseName, userName, password);
            return manager;
        }catch (Exception e) {
            throw new ServiceException("Connection error ", e);
        }
    }


//    @Override
//    public void clear(DatabaseManager manager, String tableName) {
//        manager.clear(tableName);
//    }
//
//    @Override
//    public void createdatabase(DatabaseManager manager, String databaseName) {
//        manager.createDatabase(databaseName);
//    }
//
//
//    @Override
//    public void deletedatabase(DatabaseManager manager, String dbname) {
//        manager.deleteDatabase(dbname);
//    }


}
