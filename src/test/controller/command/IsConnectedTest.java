package src.test.controller.command;

import src.view.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.controller.command.*;
import src.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IsConnectedTest {
    DatabaseManager manager;
    View view;
    Command command;

    @Before
    public void setup() {
        manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new isConnected(manager, view);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("if does not connected");
        verify(view).write("Command 'if does not connected' is not available. You must connect! ");
    }

    @Test
    public void testDescription() throws Exception {
        String description = command.description();
        assertEquals(null, description);
    }

    @Test
    public void testCommandFormat() throws Exception {
        String description = command.commandFormat();
        assertEquals(null, description);
    }
}