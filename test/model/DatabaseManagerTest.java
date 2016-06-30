package test.model;

import org.junit.Before;
import org.junit.Test;
import src.model.DataSet;
import src.model.DatabaseManager;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class DatabaseManagerTest {

    private DatabaseManager manager;
    public static final String TABLE_NAME = "t1";
    public static final String DB_NAME = "sqlcmd";
    public static final String LOGIN = "vlkvsky";
    public static final String PASSWORD = "0990";

    public abstract DatabaseManager getDataBaseManager();

    @Before
    public void setup() {
        manager = getDataBaseManager();
        manager.connect(DB_NAME, LOGIN, PASSWORD);
    }

    @Test
    public void testGetAllTablesNames() {
        String[] tablesNames = manager.getTableNames();
        assertEquals("[" + TABLE_NAME + "]", Arrays.toString(tablesNames));
    }

    @Test
    public void testGetTableData() {
        // given
        manager.clear(TABLE_NAME);

        //when
        DataSet input = new DataSet();
        input.put("name", "Vadym");
        input.put("password", "pass");
        input.put("id", 13);
        manager.create(TABLE_NAME, input);

        //then
        DataSet[] users = manager.getTableData(TABLE_NAME);
        assertEquals(1, users.length);


        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Vadym, pass, 13]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        // given
        manager.clear(TABLE_NAME);

        DataSet input = new DataSet();
        input.put("name", "Vadym");
        input.put("password", "pass");
        input.put("id", 13);
        manager.create(TABLE_NAME, input);

        //when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");
        newValue.put("name", "Nastya");
        manager.update(TABLE_NAME, 13, newValue);

        //then
        DataSet[] users = manager.getTableData(TABLE_NAME);
        assertEquals(1, users.length);


        DataSet user = users[0];
        assertEquals("[name, password, id]", Arrays.toString(user.getNames()));
        assertEquals("[Nastya, pass2, 13]", Arrays.toString(user.getValues()));

    }

    @Test
    public void testGetColumnsNames() {
        // given
        manager.clear(TABLE_NAME);
        //when
        String[] columNames = manager.getTableColumns(TABLE_NAME);

        //then

        assertEquals("[name, password, id]", Arrays.toString(columNames));
    }@Test
    public void tesIsConnected() {
        // given
        //when
        //then
       assertTrue(manager.isConnected());
    }
}