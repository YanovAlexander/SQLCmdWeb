package ua.com.juja.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PostgresManagerTest {

    private final static String DB_USERNAME = "postgres"; //change only username and password
    private final static String DB_PASSWORD = "pass";
    private final static String DB_NAME = "testing"; // db will be deleted, don't put db what you using, don't change db
    private final static String DB_NAME_SECOND = "testingdb";
    private final static String TABLE_NAME = "testing";

    private DatabaseManager manager;

    @Before
    public void init() {
        manager = new PostgresManager();
        manager.connect("", DB_USERNAME, DB_PASSWORD);
        manager.createDatabase(DB_NAME);
        manager.connect(DB_NAME, DB_USERNAME, DB_PASSWORD);
        List<String> columnParameters = new LinkedList<>();
        columnParameters.add("username");
        columnParameters.add("password");
        manager.createTable(TABLE_NAME, columnParameters);
    }

    @After
    public void clearAfterAllTests() {
        manager.connect("", DB_USERNAME, DB_PASSWORD);
        manager.deleteTable(TABLE_NAME);
        manager.deleteDatabase(DB_NAME);
    }

    @Test
    public void testGetAllTableNames() {
        // given
        manager.getTableData("testing");

        // when
        Set<String> tableNames = manager.getTableNames();

        // then
        assertEquals("[testing]", tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        // given
        manager.clear("testing");

        // when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("username", "Stiven");
        input.put("password", "Pass");
        manager.create("testing", input);

        // then
        List<DataSet> users = manager.getTableData("testing");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[id, username, password]", user.getNames().toString());
        assertEquals("[13, Stiven, Pass]", user.getValues().toString());
    }

    @Test
    public void testUpdateTableData() {
        // given
        manager.clear("testing");

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("username", "Stiven");
        input.put("password", "Pass");
        manager.create("testing", input);

        // when
        Map<String, Object> newValue = new LinkedHashMap<>();
        newValue.put("password", "pass2");
        newValue.put("username", "Pup");
        manager.update("testing", "id", "13", newValue);

        // then
        List<DataSet> users = manager.getTableData("testing");
        assertEquals(1, users.size());

        DataSet user = users.get(0);
        assertEquals("[id, username, password]", user.getNames().toString());
        assertEquals("[13, Pup, pass2]", user.getValues().toString());
    }

    @Test
    public void testDeleteRecord() {
        // given
        manager.clear("testing");

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("username", "Stiven");
        input.put("password", "Pass");
        manager.create("testing", input);
        List<DataSet> users1 = manager.getTableData("testing");

        // when
        manager.delete("testing", "13");

        // then
        List<DataSet> users = manager.getTableData("testing");

        DataSet user1 = users1.get(0);

        assertEquals("[id, username, password]", user1.getNames().toString());
        assertEquals("[13, Stiven, Pass]", user1.getValues().toString());
        assertEquals("[]", users.toString());
    }

    @Test
    public void testGetColumnNames() {
        // given
        manager.clear("testing");

        // when
        Set<String> columnNames = manager.getTableColumns("testing");

        // then
        assertEquals("[id, username, password]", columnNames.toString());
    }

    @Test
    public void testGetDatabasesList() {

        // given when
        Set<String> columnNames = new LinkedHashSet<>();
        columnNames.addAll(manager.databasesList());

        // then
        assertEquals("[postgres, testing, iuo]", columnNames.toString());
    }

}
