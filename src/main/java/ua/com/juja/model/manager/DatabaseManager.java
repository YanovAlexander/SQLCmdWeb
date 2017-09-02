package ua.com.juja.model.manager;

import ua.com.juja.model.dataSet.DataSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for work with Database layer.
 */

public interface DatabaseManager {

    /**
     * Getting all tables of the required database
     * If no tables in current Database, he come back empty Set<String>
     *
     * @return Set<String> of exist table names
     */
    Set<String> getTableNames();

    /**
     * Getting data from required table.
     * If table is empty, he come back empty List<DataSet>
     *
     * @param tableName name of the required table
     * @return List<DataSet> with table data
     */
    List<DataSet> getTableData(String tableName);

    /**
     * Connection method insertRecord connection to required database.
     *
     * @param databaseName name of the database
     * @param userName     name of registered user
     * @param password     user password
     * @throws DatabaseManagerException if can't find driver or wrong user parameters
     */
    void connect(String databaseName, String userName, String password);

    /**
     * Clear required table. All data will be deleted.
     *
     * @param tableName name of the required table
     */
    void clearTable(String tableName);

    /**
     * Insert record to current table.
     *
     * @param tableName  name of the table
     * @param columnData names of columns and value, that will be put to this columns.
     */
    void insertRecord(String tableName, Map<String, Object> columnData);

    /**
     * Update values of record.
     *
     * @param tableName  name of the table with needed record
     * @param keyValue   value of the record id
     * @param columnData value of all other columns exclude id column
     */
    void updateRecord(String tableName, Integer keyValue, Map<String, Object> columnData);

    /**
     * Getting column names of required table
     *
     * @param tableName name of the required table
     * @return Set<String> with column names inside
     */
    Set<String> getTableColumns(String tableName);

    /**
     * Creating database using databaseName
     *
     * @param databaseName name of database which will be created.
     */
    void createDatabase(String databaseName);

    /**
     * List of existing databases
     *
     * @return Set<String> where every element is name of exist database
     */
    Set<String> databasesList();

    /**
     * Drop required table with all data.
     *
     * @param tableName name of exist table for deleteRecord
     */
    void deleteTable(String tableName);

    /**
     * Deleting exist record from table.
     *
     * @param tableName name of table with  record
     * @param keyValue  value of id column
     */
    void deleteRecord(String tableName, String keyValue);

    /**
     * Deleting database.
     *
     * @param databaseName name of required database
     */
    void deleteDatabase(String databaseName);

    /**
     * Creating table using tableName and columnParameter.
     *
     * @param tableName        name of new table
     * @param columnParameters column names and column
     *                         types exclude identifier column id with value
     */
    void createTable(String tableName, List<String> columnParameters);

    /**
     * Getting name of database which user is already connected
     *
     * @return name of database.
     */
    String getDatabaseName();

    /**
     * Getting name of user which already connected
     *
     * @return name of user.
     */
    String getUserName();
}
