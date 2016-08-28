package ua.com.vlkvsky.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class DefaultConnectTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = Mockito.mock(DatabaseManager.class);
        view = Mockito.mock(View.class);
        command = new DefaultConnect(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("default connect");
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcess() throws Exception {
        boolean canProcess = command.canProcess("defaulttt ");
        assertFalse(canProcess);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("default connect");
        verify(view).write("Connection to default DB successful. To see the available commands, type <help>");
    }

    @Test
    public void testProcessWithWrongParameters() throws Exception {
        try {
            command.process("default this mistake");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'default connect', but expected: default this mistake", e.getMessage());
        }
    }
}