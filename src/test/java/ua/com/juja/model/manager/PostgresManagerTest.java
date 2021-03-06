package ua.com.juja.model.manager;

import org.junit.*;
import ua.com.juja.model.dataSet.DataSet;
import ua.com.juja.model.manager.DatabaseManager;
import ua.com.juja.model.manager.PostgresManager;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PostgresManagerTest {

    private final static String DB_USERNAME = "postgres"; //change only username and password
    private final static String DB_PASSWORD = "pass";
    private final static String DB_NAME = "testing"; // db will be deleted, don't put db what you using, don't change db
    private final static String DB_NAME_SECOND = "testingdb";
    private final static String TABLE_NAME = "testing";

    private static DatabaseManager manager;

    @BeforeClass
    public static void init() {
        manager = new PostgresManager();
        manager.connect("", DB_USERNAME, DB_PASSWORD);
        manager.createDatabase(DB_NAME);
        manager.connect(DB_NAME, DB_USERNAME, DB_PASSWORD);
        List<String> columnParameters = new LinkedList<>();
        columnParameters.add("username");
        columnParameters.add("password");
        manager.createTable(TABLE_NAME, columnParameters);
    }

    @AfterClass
    public static void clearAfterAllTests() {
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
        manager.clearTable("testing");

        // when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("username", "Stiven");
        input.put("password", "Pass");
        manager.insertRecord("testing", input);

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
        manager.clearTable("testing");

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("username", "Stiven");
        input.put("password", "Pass");
        manager.insertRecord("testing", input);

        // when
        Map<String, Object> newValue = new LinkedHashMap<>();
        newValue.put("password", "pass2");
        newValue.put("username", "Pup");
        manager.updateRecord("testing", 13,  newValue);

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
        manager.clearTable("testing");

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 13);
        input.put("username", "Stiven");
        input.put("password", "Pass");
        manager.insertRecord("testing", input);
        List<DataSet> users1 = manager.getTableData("testing");

        // when
        manager.deleteRecord("testing", "13");

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
        manager.clearTable("testing");

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
        assertEquals("[postgres, newrrr, testing]", columnNames.toString());
    }

}
