package ua.com.vlkvsky.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.vlkvsky.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class HelpTest {

    private View view;
    private Command command;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Help(view);

    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("help");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWrongCommand() throws Exception {
        boolean canNotProcess = command.canProcess("helpBadWay");
        assertFalse(canNotProcess);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("help");
        verify(view).write(
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
                "+----------------+---------------------------------+");
    }
}