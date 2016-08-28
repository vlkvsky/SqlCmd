package ua.com.vlkvsky;

import ua.com.vlkvsky.model.DatabaseManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class Support {

    private static Configuration configuration = new Configuration();
    private static final String TEST_DB = configuration.getTestDb();
    private static final String EXISTING_DB = configuration.getDbName();
    private static final String USER = configuration.getUsername();
    private static final String PASSWORD = configuration.getPassword();

    public static void setupData(DatabaseManager manager) {
        try {
            manager.connect(EXISTING_DB, USER, PASSWORD);
        } catch (RuntimeException e) {
            throw new RuntimeException("First you should enter data to 'configuration/sqlcmd.properties'"
                    + "\n" + e.getCause());
        }
        try {
            manager.deleteDB(TEST_DB);
        } catch (RuntimeException e) {
//            System.out.println("Test DB does not exist. Creating... ");   // if Test DB does not exist
        }
        try {
            manager.createDB(TEST_DB);
        } catch (RuntimeException e) {
            throw new RuntimeException("Can not create Test DB. " + e.getMessage());
        }

        try {
            manager.connect(TEST_DB, USER, PASSWORD);
        } catch (RuntimeException e) {
            throw new RuntimeException("Can not connect to Test DB. " + e.getMessage());
        }
        try {
            createTablesWithData(manager);
        } catch (RuntimeException e) {
            throw new RuntimeException("Can not edit tables in Test DB. " + e.getMessage());
        }
    }

    public static void deleteData(DatabaseManager manager) {
        try {
            manager.connect(EXISTING_DB, USER, PASSWORD);
            manager.deleteDB(TEST_DB);
        } catch (RuntimeException e) {
            throw new RuntimeException("AfterClass deleting error: " + e.getCause());
        }
    }

    private static void createTablesWithData(DatabaseManager manager) {
        manager.createTable("users" +
                " (id SERIAL PRIMARY KEY, name VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL)");
        manager.createTable("test1 (id SERIAL PRIMARY KEY)");
        manager.createTable("users2" +
                " (id SERIAL NOT NULL PRIMARY KEY,username varchar(225) NOT NULL UNIQUE, password varchar(225))");
        Map<String, Object> dataSet = new LinkedHashMap<>();
        dataSet.put("id", "0");
        dataSet.put("name", "NameFromSetup");
        dataSet.put("password", "PasswordFromSetup");
        manager.insert("users", dataSet);
    }
}
