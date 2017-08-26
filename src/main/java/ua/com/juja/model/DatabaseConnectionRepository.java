package ua.com.juja.model;

import org.springframework.data.repository.CrudRepository;
import ua.com.juja.model.entity.DatabaseConnection;


public interface DatabaseConnectionRepository extends CrudRepository<DatabaseConnection, Integer> {

    DatabaseConnection findByUserNameAndDbName(String userName, String dbName);
}
