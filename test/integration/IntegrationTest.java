package test.integration;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import src.Main;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }


    public String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                "Available commands:\r\n" +
                "\t connect|dataBaseName|userName|password\r\n" +
                "\t\t Connect to DB 'dataBaseName'\r\n" +
                "\t help\r\n" +
                "\t\t Recieve list of all available commands\r\n" +
                "\t find|tableName\r\n" +
                "\t\t Recieve the contents of the table 'tableName'\r\n" +
                "\t list\r\n" +
                "\t\t Recieve list of all tables\r\n" +
                "\t clear|tableName\r\n" +
                "\t\t Delete all values of 'tableName'\r\n" +
                "\t create|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\t CreateTable values to 'tableName'\r\n" +
                "\t exit\r\n" +
                "\t\t Close the connection\r\n" +
                "Enter the command or 'help'\r\n" +
                "See you later!\r\n", getData());
    }

    @Test
    public void testExit() {
        // given
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //list
                "Command 'list' isn't allow. You must connect! \r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }
    @Test
    public void testFindWithoutConnect() {
        // given
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //list
                "Command 'find|user' isn't allow. You must connect! \r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }
    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //list
                "Command 'unsupported' isn't allow. You must connect! \r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //unsupported
                "non-existent request: 'unsupported'\r\n" +
                        "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }
    @Test
    public void testListAfterConnect() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("list");
        in.add("connect|sqlcmd2|vlkvsky|0990");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //list
                "[t1]\r\n" +
                "Enter the command or 'help'\r\n" +
                //connect|sqlcmd2|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //list
                "[t2]\r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("find|t1");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //find|t1
                "------------------\r\n" +
                "|name|password|id|\r\n" +
                "------------------\r\n" +
                "------------------\r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("list");
        in.add("connect|sqlcmd2|vlkvsky|0990");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                // connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //list
                "[t1]\r\n" +
                "Enter the command or 'help'\r\n" +
                //connect|sqlcmd2|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                // list
                "[t2]\r\n" +
                "Enter the command or 'help'\r\n" +
                // exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testWithError() {
        // given
        in.add("connect|sqlcmd");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|
                "Can't perform the action! Problem: Conversion = '%'\r\n" +
                "Repeat one more time:\r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testFindAfterConnect_withData() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("clear|t1");
        in.add("create|t1|id|13|name|Vadym|password|*****");
        in.add("create|t1|id|14|name|Nastya|password|+++++");
        in.add("find|t1");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //clear|t1
                "Table 't1' cleared\r\n" +
                "Enter the command or 'help'\r\n" +
                //create|t1|id|1|name|Vadym|password|*****
                "Value '{names: [id, name, password],values: [13, Vadym, *****],}' added to table 't1'\r\n" +
                "Enter the command or 'help'\r\n" +
                //create|t1|id|2|name|Nastya|password|+++++
                "Value '{names: [id, name, password],values: [14, Nastya, +++++],}' added to table 't1'\r\n" +
                        "Enter the command or 'help'\r\n" +
                //find|t1
                "------------------\r\n" +
                "|name|password|id|\r\n" +
                "------------------\r\n" +
                "|Vadym|*****|13|\r\n" +
                "|Nastya|+++++|14|\r\n" +
                "------------------\r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testClearWithError() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("clear|xxx|xxx|xxx");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //clear|xxx|xxx|xxx
                "Can't perform the action! Problem: Expected format is 'clear|tableName'. But actual 'clear|xxx|xxx|xxx'\r\n" +
                "Repeat one more time:\r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }

    @Test
    public void testCreateWithError() {
        // given
        in.add("connect|sqlcmd|vlkvsky|0990");
        in.add("create|t1|error");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990\r\n" +
                //connect|sqlcmd|vlkvsky|0990
                "Connection successful!\r\n" +
                "Enter the command or 'help'\r\n" +
                //clear|t1
                "Can't perform the action! Problem: Must be an even number of parameters. " +
                "Example: 'create|tableName|column1|value1|column2|value2|...|columnN|valueN'. But actual: 'create|t1|error'\r\n" +
                "Repeat one more time:\r\n" +
                "Enter the command or 'help'\r\n" +
                //exit
                "See you later!\r\n", getData());
    }
}