package ua.com.vlkvsky.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class DefaultConnectTest {
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
        boolean canProcess = command.canProcess("connect");
        assertTrue(canProcess);
    }

    @Test
    public void testCanNotProcess() throws Exception {
        boolean canProcess = command.canProcess("error");
        assertFalse(canProcess);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("connect");
        verify(view).write("Connection successful. To see the available commands, type <help>");
    }
}