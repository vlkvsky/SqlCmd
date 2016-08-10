package src.test.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.controller.command.Command;
import src.controller.command.isConnected;
import src.model.DatabaseManager;
import src.view.View;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class IsConnectedTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = Mockito.mock(DatabaseManager.class);
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