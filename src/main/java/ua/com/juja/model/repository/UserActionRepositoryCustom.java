package ua.com.juja.model.repository;

public interface UserActionRepositoryCustom {

    void saveAction(String databaseName, String userName, String action);
}
