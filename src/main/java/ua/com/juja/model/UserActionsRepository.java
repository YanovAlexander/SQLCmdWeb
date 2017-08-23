package ua.com.juja.model;

import org.springframework.data.repository.CrudRepository;
import ua.com.juja.model.entity.UserAction;

import java.util.List;

public interface UserActionsRepository extends CrudRepository<UserAction, Integer> {

    List<UserAction> findByUserName(String userName);
}
