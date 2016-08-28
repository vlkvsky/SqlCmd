package ua.com.vlkvsky;

import ua.com.vlkvsky.model.DatabaseManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class Support {

    private static Configuration configuration = new Configuration();
    private static final String TEST_DB = configuration.getTestDb();
    private static final String CREATED_DATABASE = configuration.getDbName();
    private static final String USER = configuration.getUsername();
    private static final String PASSWORD = configuration.getPassword();

    public static void setupData(DatabaseManager manager) {
        try {
            manager.connect(CREATED_DATABASE, USER, PASSWORD);
        } catch (RuntimeException e) {
            throw new RuntimeException("Для работы тестов измените имя и пароль в 'configuration/sqlcmd.properties'"
                    + "\n" + e.getCause());
        }
        try {
            manager.createDB(TEST_DB);
        } catch (RuntimeException e) {
            throw new RuntimeException("Невозможно создать тестовую базу данных."
                    + "\n" + e.getCause());
        }
        try {
            manager.connect(TEST_DB, USER, PASSWORD);

        } catch (RuntimeException e) {
            throw new RuntimeException("Невозможно подключиться к тестовой базе данных."
                    + "\n" + e.getCause());
        }
        try {
            createTablesWithData(manager);

        } catch (RuntimeException e) {
            throw new RuntimeException("Невозможно редактировать таблицы в тестовой базе данных."
                    + "\n" + e.getCause());
        }

    }

    public static void deleteData(DatabaseManager manager) {
        try {
            manager.connect(CREATED_DATABASE, USER, PASSWORD);
            manager.deleteDB(TEST_DB);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        dataSet.put("password", "PasswordFromSupport");
        manager.insert("users", dataSet);
    }
}
