package ua.com.juja.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ua.com.juja.model.entity.UserAction;

import java.util.List;

public interface UserActionsRepository extends CrudRepository<UserAction, Integer> {

    @Query(value = "SELECT ua FROM UserAction ua WHERE ua.connection.userName = :userName")
    List<UserAction> findByUserName(@Param("userName") String userName);
}
