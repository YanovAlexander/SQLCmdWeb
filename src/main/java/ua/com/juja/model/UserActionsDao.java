package ua.com.juja.model;

import java.util.List;

public interface UserActionsDao {

    void log(String userName, String dbName, String action);

    List<UserAction> getAllFor(String userName);
}
