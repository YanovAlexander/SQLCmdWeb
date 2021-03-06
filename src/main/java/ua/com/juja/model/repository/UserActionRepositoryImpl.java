package ua.com.juja.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.com.juja.model.entity.DatabaseConnection;
import ua.com.juja.model.entity.UserAction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserActionRepositoryImpl implements UserActionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DatabaseConnectionRepository databaseConnections;

    @Override
    @Transactional
    public void saveAction(String databaseName, String userName, String action) {
        DatabaseConnection databaseConnection =
                databaseConnections.findByUserNameAndDbName(userName, databaseName);
        if (databaseConnection == null) {
            databaseConnection = databaseConnections.save(
                    new DatabaseConnection(userName, databaseName));
        }
        entityManager.persist(new UserAction(action,
                databaseConnection));
    }
}
