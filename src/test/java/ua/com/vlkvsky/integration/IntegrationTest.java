package ua.com.vlkvsky.integration;

import org.junit.*;
import ua.com.vlkvsky.Main;
import ua.com.vlkvsky.Support;
import ua.com.vlkvsky.controller.MainController;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.model.PostgresManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private static MainController.Configuration configuration = new MainController.Configuration();
    private static DatabaseManager manager;

    private final String TEST_DB = configuration.getTestDb();
    private final String EXISTING_DATABASE = configuration.getDbName();
    private final String USER = configuration.getUsername();
    private final String PASSWORD = configuration.getPassword();

    private final String commandConnect = "connect " + TEST_DB + " " + USER + " " + PASSWORD;
    private final String commandDisconnect = "connect " + EXISTING_DATABASE + " " + USER + " " + PASSWORD;
    private final String pleaseConnect = "Welcome " + System.getProperty("user.name") + "!\n" +
            "Enter DB name, login, password in the format: connect DATABASE USER PASSWORD\n";
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @BeforeClass
    public static void buildDatabase() {
        manager = new PostgresManager();
        Support.setupData(manager);
    }

    @AfterClass
    public static void DeleteDB() {
        Support.deleteData(manager);
    }

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8").replaceAll("\r\n", "\n");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testHelpBeforeConnect() {
        // given
        in.add("help");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // help
                "Command 'help' is not available. You must connect! \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testExit() {
        // given
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // exit
                "See you later!\n", getData());
    }

    @Test
    public void testEx() {
        // given
        in.add("ex");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // exit
                "See you later!\n", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        // given
        in.add("tables");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // tables
                "Command 'tables' is not available. You must connect! \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testContentBeforeConnect() {
        // given
        in.add("content t1");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // find user
                "Command 'content t1' is not available. You must connect! \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testContentAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("content users");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "+--+-------------+-----------------+\n" +
                "|id|name         |password         |\n" +
                "+--+-------------+-----------------+\n" +
                "|0 |NameFromSetup|PasswordFromSetup|\n" +
                "+--+-------------+-----------------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testUnsupportedBeforeConnect() {
        // given
        in.add("unsupported");
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // unsupported
                "Command 'unsupported' is not available. You must connect! \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("unsupported");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // unsupported
                "non-existent request: 'unsupported'\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // exit
                "See you later!\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect " + TEST_DB);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                "Can't perform the action! Problem: Format 'connect <> <> <>', but expected: connect dbintegrationtest\n" +
                "Repeat one more time.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testTablesAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("tables");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Available tables: users, test1, users2\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testInsertAfterConnect_withData() {
        // given
        in.add(commandConnect);

        in.add("insert users");
        in.add("1");
        in.add("Vadym");
        in.add("*****");

        in.add("insert users");
        in.add("2");
        in.add("Nastya");
        in.add("+++++");

        in.add("deleteRow users 1");
        in.add("Y");
        in.add("deleteRow users 2");
        in.add("Y");

        in.add("content users");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'name' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                "Successfully added to the table 'users' this data:\n" +
                "+--+-----+--------+\n" +
                "|id|name |password|\n" +
                "+--+-----+--------+\n" +
                "|1 |Vadym|*****   |\n" +
                "+--+-----+--------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'name' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                "Successfully added to the table 'users' this data:\n" +
                "+--+------+--------+\n" +
                "|id|name  |password|\n" +
                "+--+------+--------+\n" +
                "|2 |Nastya|+++++   |\n" +
                "+--+------+--------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Are you sure you want to delete '1'? Y/N\n" +
                "Row with id '1' successfully deleted.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Are you sure you want to delete '2'? Y/N\n" +
                "Row with id '2' successfully deleted.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "+--+-------------+-----------------+\n" +
                "|id|name         |password         |\n" +
                "+--+-------------+-----------------+\n" +
                "|0 |NameFromSetup|PasswordFromSetup|\n" +
                "+--+-------------+-----------------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add(commandConnect);
        in.add("clear sadfasd fsf fdsf");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then

        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                //clear sadfasd fsf fdsf
                "Can't perform the action! Problem: Expected format is 'clear <table>'. But actual 'clear sadfasd fsf fdsf'\n" +
                "Repeat one more time.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                //commandDisconnect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                //exit
                "See you later!\n", getData());
    }

    @Test
    public void testInsertWithErrors() {
        // given
        in.add(commandConnect);
        in.add("insert user error");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // insert user error
                "Can't perform the action! Problem: Format 'insert <>', but expected: insert user error\n" +
                "Repeat one more time.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // commandDisconnect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // exit
                "See you later!\n", getData());
    }

    @Test
    public void testCreateTableSimple() {
        // given
        in.add(commandConnect);
        in.add("create");
        in.add("users5");
        in.add("id");
        in.add("name");
        in.add("password");
        in.add("5");
        in.add("deleteTable users5");
        in.add("Y");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                "Name for a new table: users5\n" +
                "Enter name to column of PRIMARY KEY(the name must begin with a letter!) or '0' to main menu.\n" +
                "Column name of PRIMARY KEY: id\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                "The name of the next column 'name'\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                "The name of the next column 'password'\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                "Table users5 created.\n" +
                "+--+----+--------+\n" +
                "|id|name|password|\n" +
                "+--+----+--------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Are you sure you want to delete 'users5'? Y/N\n" +
                "Table 'users5' successfully deleted.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testTableSimpleExit() {
        // given
        in.add(commandConnect);
        in.add("create");
        in.add("");
        in.add("1");
        in.add("0");
        in.add("create");
        in.add("user6");
        in.add("");
        in.add("0");
        in.add("create");
        in.add("user6");
        in.add("id");
        in.add("");
        in.add("0");
        in.add("create");
        in.add("user6");
        in.add("id");
        in.add("name");
        in.add("0");

        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // "create"
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                // ""
                "Enter the name for a new table, but you enter an empty string.\n" +
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                // "1"
                "The name must begin with a letter, but not with: '1'\n" +
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                // "0"
                " \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // "create"
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                // "user6"
                "Name for a new table: user6\n" +
                "Enter name to column of PRIMARY KEY(the name must begin with a letter!) or '0' to main menu.\n" +
                // ""
                "Enter name to column of PRIMARY KEY, but not an empty string!\n" +
                "Enter name to column of PRIMARY KEY(the name must begin with a letter!) or '0' to main menu.\n" +
                // "0"
                " \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // "create"
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                // "user6"
                "Name for a new table: user6\n" +
                "Enter name to column of PRIMARY KEY(the name must begin with a letter!) or '0' to main menu.\n" +
                // "id"
                "Column name of PRIMARY KEY: id\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                // ""
                "Enter the column name, but you enter an empty string\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                // "0"
                " \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // "create"
                "Enter the name for a new table:(the name must begin with a letter!) or '0' to main menu.\n" +
                // "user6"
                "Name for a new table: user6\n" +
                "Enter name to column of PRIMARY KEY(the name must begin with a letter!) or '0' to main menu.\n" +
                // "id"
                "Column name of PRIMARY KEY: id\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                // "name"
                "The name of the next column 'name'\n" +
                "Enter the name of the next column(the name must begin with a letter!) or '5' to finish creating or '0' to main menu.\n" +
                // "0"
                " \n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // "connect sqlcmd USER PASSWORD
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // ex
                "See you later!\n", getData());
    }

    @Test
    public void testInsertSimple() {
        // given
        in.add(commandConnect);
        in.add("insert users2");
        in.add("1");
        in.add("Vadym");
        in.add("*****");
        in.add("clear users2");
        in.add("y");
        in.add(commandDisconnect);
        in.add("ex");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // insert users2
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                // 1
                "Enter a value in the field 'username' or enter '0' to exit to the main menu .\n" +
                // Vadym
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                // *****
                "Successfully added to the table 'users2' this data:\n" +
                "+--+--------+--------+\n" +
                "|id|username|password|\n" +
                "+--+--------+--------+\n" +
                "|1 |Vadym   |*****   |\n" +
                "+--+--------+--------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // clear users2
                "Are you sure you want to delete 'users2'? Y/N\n" +
                "Table 'users2' cleared\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // connect sqlcmd USER PASSWORD
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // ex
                "See you later!\n", getData());
    }

    @Test
    public void testInsertToNonExistenTable() {
        // given
        in.add(commandConnect);
        in.add("insert NonExistenTable");
        in.add("insert users2");
        in.add("1");
        in.add("Vadym");
        in.add("*****");
        in.add("clear users2");
        in.add("y");
        in.add(commandDisconnect);
        in.add("ex");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Table 'NonExistenTable' not found. Try insert to another table.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // insert users2
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                // 1
                "Enter a value in the field 'username' or enter '0' to exit to the main menu .\n" +
                // Vadym
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                // *****
                "Successfully added to the table 'users2' this data:\n" +
                "+--+--------+--------+\n" +
                "|id|username|password|\n" +
                "+--+--------+--------+\n" +
                "|1 |Vadym   |*****   |\n" +
                "+--+--------+--------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // clear users2
                "Are you sure you want to delete 'users2'? Y/N\n" +
                "Table 'users2' cleared\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // connect sqlcmd USER PASSWORD
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // ex
                "See you later!\n", getData());
    }

    @Test
    public void testInsertSimpleExit() {
        // given
        in.add(commandConnect);
        in.add("insert users2");
        in.add("");
        in.add("1");
        in.add("0");
        in.add("insert users2");
        in.add("1");
        in.add("");
        in.add("Vadym");
        in.add("0");
        in.add("insert users2");
        in.add("1");
        in.add("Vadym");
        in.add("");
        in.add("0");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                "Enter name to column of PRIMARY KEY, but not an empty string!\n" +
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'username' or enter '0' to exit to the main menu .\n" +
                "Main menu\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'username' or enter '0' to exit to the main menu .\n" +
                "Enter name to column of PRIMARY KEY, but not an empty string!\n" +
                "Enter a value in the field 'username' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                "Main menu\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Enter a value in the field 'id' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'username' or enter '0' to exit to the main menu .\n" +
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                "Enter name to column of PRIMARY KEY, but not an empty string!\n" +
                "Enter a value in the field 'password' or enter '0' to exit to the main menu .\n" +
                "Main menu\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testDeleteDBException() {
        // given
        in.add(commandConnect);
        in.add("deleteDB sqlcmd_DONT_EXIST");
        in.add("y");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then

        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Are you sure you want to delete 'sqlcmd_DONT_EXIST'? Y/N\n" +
                "Can't perform the action! Problem: ERROR: database \"sqlcmd_dont_exist\" does not exist\n" +
                "Repeat one more time.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Test
    public void testHelpAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("help");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                // help
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Available commands:\n" +
                "+----------------+---------------------------------+\n" +
                "|Command         |Description                      |\n" +
                "+----------------+---------------------------------+\n" +
                "|help            |Get available commands           |\n" +
                "|connect <> <> <>|Connect to <DB> <User> <Password>|\n" +
                "|DBs             |Get all DataBases                |\n" +
                "|tables          |Get all tables of DB             |\n" +
                "|createDB <>     |Create <DB>                      |\n" +
                "|create          |Create table step-by-step        |\n" +
                "|content <>      |Get content of <table>           |\n" +
                "|insert <>       |Add data to <table>              |\n" +
                "|deleteRow <> <> |Delete row from <table> with <id>|\n" +
                "|clear <>        |Clear data of <table>            |\n" +
                "|deleteTable <>  |Delete <table>                   |\n" +
                "|deleteDB <>     |Delete <DB>                      |\n" +
                "|exit            |Close application                |\n" +
                "+----------------+---------------------------------+\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Ignore // проходит, но занимает много времени
    @Test
    public void testConnectAfterConnect() {
        // given
        in.add(commandConnect);
        in.add("createDB testconnectafterconnect");
        in.add("connect testconnectafterconnect " + USER + " " + PASSWORD);
        in.add("tables");
        in.add(commandDisconnect);
        in.add("deleteDB testconnectafterconnect");
        in.add("Y");

        in.add("exit");
        // when
        Main.main(new String[0]);
        // then
        assertEquals(pleaseConnect +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "DB 'testconnectafterconnect' created.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "DB is empty.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "Are you sure you want to delete 'testconnectafterconnect'? Y/N\n" +
                "DB 'testconnectafterconnect' successfully deleted\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                "See you later!\n", getData());
    }

    @Ignore // Проходит, но занимает много времени
    @Test
    public void testCreateDeleteDatabase() {
        // given
        in.add(commandConnect);
        in.add("createDB testCreateDeleteDatabase");
        in.add("deleteDB testCreateDeleteDatabase");
        in.add("y");
        in.add(commandDisconnect);
        in.add("exit");
        // when
        Main.main(new String[0]);
        // then

        assertEquals(pleaseConnect +
                // connect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // createDB testCreateDeleteDatabase
                "DB 'testCreateDeleteDatabase' created.\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // deleteDB testCreateDeleteDatabase
                "Are you sure you want to delete 'testCreateDeleteDatabase'? Y/N\n" +
                // y
                "DB 'testCreateDeleteDatabase' successfully deleted\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // commandDisconnect
                "Connection successful. To see the available commands, type <help>\n" +
                "-----------------\n" +
                "Enter the command:\n" +
                // ex
                "See you later!\n", getData());
    }
}
