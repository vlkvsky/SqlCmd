package project.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import project.BeforeTestsChangeNameAndPass;
import project.Support;
//import DatabaseManager;
//import PostgresManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private static final String DATABASE = BeforeTestsChangeNameAndPass.DATABASE;
    private static final String USER = BeforeTestsChangeNameAndPass.USER;
    private static final String PASSWORD = BeforeTestsChangeNameAndPass.PASSWORD;
    private static DatabaseManager manager;

    @BeforeClass
    public static void setup() {
        manager = new PostgresManager();
        Support.setupData(manager);
    }

    @AfterClass
    public static void dropDatabase() {
        Support.deleteData(manager);
    }

    @Test
    public void testGetAllTableNames() {
        Set<String> tableNames = manager.getTableNames();
        assertEquals("[users, users2, test1]", tableNames.toString());
    }

    @Test
    public void testGetTableData() {
        // given
        manager.clear("users");
        // when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("id", 11);
        input.put("name", "Stiven");
        input.put("password", "****");
        manager.insert("users", input);
        // then
        List<Map<String, Object>> users = manager.getTableData("users");
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[11, Stiven, ****]", user.values().toString());
        assertEquals("[id, name, password]", user.keySet().toString());
    }

    @Test
    public void testGetTableData2() {
        // given
        manager.clear("users");
        // when
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "****");
        input.put("id", 11);
        manager.insert("users", input);

        Map<String, Object> input2 = new LinkedHashMap<>();
        input2.put("name", "Stiven2");
        input2.put("password", "*****");
        input2.put("id", 12);
        manager.insert("users", input2);
        // then
        List<Map<String, Object>> users = manager.getTableData("users");
        assertEquals(2, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[11, Stiven, ****]", user.values().toString());
        assertEquals("[id, name, password]", user.keySet().toString());

        Map<String, Object> user2 = users.get(1);
        assertEquals("[12, Stiven2, *****]", user2.values().toString());
        assertEquals("[id, name, password]", user2.keySet().toString());
    }

    @Test
    public void testUpdateTableData() {
        // given
        manager.clear("users");

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 15);
        manager.insert("users", input);
        // when
        Map<String, Object> newValue = new LinkedHashMap<>();
        newValue.put("password", "pass2");
        newValue.put("name", "Pup");
        manager.update("users", 15, newValue);
        // then
        List<Map<String, Object>> users = manager.getTableData("users");
        assertEquals(1, users.size());

        Map<String, Object> user = users.get(0);
        assertEquals("[id, name, password]", user.keySet().toString());
        assertEquals("[15, Pup, pass2]", user.values().toString());
    }

    @Test
    public void testGetColumnNames() {
        // when
        Set<String> columnNames = manager.getTableColumns("users");
        // then
        assertEquals("[id, name, password]", columnNames.toString());
    }

    @Test
    public void getTableSize() {
        int size = manager.getTableSize("users");
        assertEquals(1, size);
    }

    @Test
    public void clearTable() {
        // given
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("name", "Stiven");
        input.put("password", "pass");
        input.put("id", 17);
        manager.insert("users", input);
        // when
        manager.clear("users");
        // then
        List<Map<String, Object>> users = manager.getTableData("users");
        assertEquals(0, users.size());
    }

    @Test
    public void testisConnected() {
        assertTrue(manager.isConnected());
    }

    @Test
    public void tablesList() {
        Set<String> tables = manager.getTableNames();
        assertEquals("[users, test1, users2]", tables.toString());
    }

    @Test
    public void dropTable() {
        manager.deleteTable("test1");
        Set<String> tables = manager.getTableNames();
        assertEquals("[users, users2]", tables.toString());
        manager.createTable("test1(id SERIAL NOT NULL PRIMARY KEY,username varchar(225) NOT NULL UNIQUE, password varchar(225))");
    }

    @Test
    public void getters() {
        assertEquals(DATABASE, manager.getDatabaseName());
        assertEquals(USER, manager.getUser());
        assertEquals(PASSWORD, manager.getPassword());

    }
}
