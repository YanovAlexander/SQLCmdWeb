package ua.com.juja.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.model.DataSet;
import ua.com.juja.model.DataSetImpl;
import ua.com.juja.model.DatabaseManager;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ServiceImplTest {

    @InjectMocks
    private Service service;

    @Mock
    private DatabaseManager manager;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws ServiceException {
        // given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("name", "Stiven");
        input.put("password", "Pass");
        manager.create("users", input);

        Map<String, Object> input2 = new LinkedHashMap<>();
        input2.put("id", 14);
        input2.put("name", "Eva");
        input2.put("password", "PassPass");
        manager.create("users", input2);

        // when
        List<List<String>> users = service.find(manager, "users");

        // then
        assertEquals("[[name, password, id]," +
                                " [Stiven, Pass, 13], " +
                                "[Eva, PassPass, 14]]", users.toString());
    }
}
