package ua.com.juja.service;

import java.util.List;

public interface Service {

    List<String> commandsList();

    void connect(String databaseName, String userName, String password);
}
