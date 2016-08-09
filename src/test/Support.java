package src.test;

import src.model.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class Support {

    private static final String DATABASE = BeforeTestsChangeNameAndPass.DATABASE;
    private static final String USER = BeforeTestsChangeNameAndPass.USER;
    private static final String PASSWORD = BeforeTestsChangeNameAndPass.PASSWORD;

    public static void setupData(DatabaseManager manager) {
        try {
            manager.connect("sqlcmd", USER, PASSWORD);
        } catch (RuntimeException e) {
            throw new RuntimeException("Для работы тестов измените имя и пароль в классе BeforeTestsChangeNameAndPass."
                    + "\n" + e.getCause());
        }
        try {
            manager.createDB(DATABASE);
        } catch (RuntimeException e){
            throw new RuntimeException("Невозможно создать тестовую базу данных."
                    + "\n" + e.getCause());
        }
        try {
            manager.connect(DATABASE, USER, PASSWORD);

        }  catch (RuntimeException e){
            throw new RuntimeException("Невозможно подключиться к тестовой базе данных."
                    + "\n" + e.getCause());
        }
        try {
            createTablesWithData(manager);

        }  catch (RuntimeException e){
            throw new RuntimeException("Невозможно редактировать таблицы в тестовой базе данных."
                    + "\n" + e.getCause());
        }

    }

    public static void deleteData(DatabaseManager manager) {
        try {
            manager.connect("sqlcmd", USER, PASSWORD);
            manager.deleteDB(DATABASE);
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
