package ua.com.juja.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.model.manager.DatabaseManager;

import java.util.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
public class ServiceImplTest {

    @Autowired
    private Service service ;

    @Test
    public void test() throws ServiceException {
        DatabaseManager manager = service.connect("databse", "user", "password");
        // given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "Pass");
        manager.insertRecord("users", input);

        Map<String, Object> input2 = new LinkedHashMap<>();
        input2.put("id", 14);
        input2.put("name", "Eva");
        input2.put("password", "PassPass");
        manager.insertRecord("users", input2);

        // when
        List<List<String>> users = service.find(manager, "users");

        // then
        assertEquals("[[name, password, id]," +
                                " [Stiven, Pass, 13], " +
                                "[Eva, PassPass, 14]]", users.toString());
    }
}
