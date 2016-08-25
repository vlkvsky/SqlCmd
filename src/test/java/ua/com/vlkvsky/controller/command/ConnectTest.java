package ua.com.vlkvsky.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class ConnectTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new Connect(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("connect|");
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcess() throws Exception {
        boolean canProcess = command.canProcess("conneeeect|");
        assertFalse(canProcess);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("connect|databaseName|userName|password");
        verify(view).write("Connection successful. To see the available commands, type <help>");
    }

    @Test
    public void testProcessWithWrongParameters() throws Exception {
        try {
            command.process("connect|databaseName|userName");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'connect|DB|user|password', but expected: connect|databaseName|userName", e.getMessage());
        }
    }
}