package ua.com.vlkvsky.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClearTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ClearTable(manager, view);
    }

    @Test
    public void testClearTable() {
        command.process("clear user");
        verify(manager).clear("user");
        verify(view).write("Table 'user' cleared");
    }


    @Test
    public void testCantProcess() {
        boolean canProcess = command.canProcess("clear user");
        assertTrue(canProcess);
    }

    @Test
    public void testClearTableWrongCommand() {
        boolean canNotProcess = command.canProcess("cleardf user");
        assertFalse(canNotProcess);
    }

    @Test
    public void testCantProcessClearWithoutParametersString() {
        boolean canProcess = command.canProcess("clear ");
        assertTrue(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Expected format is 'clear <table>'. But actual 'clear'", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        try {
            command.process("clear table qwe");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Expected format is 'clear <table>'. But actual 'clear table qwe'", e.getMessage());
        }
    }
}
