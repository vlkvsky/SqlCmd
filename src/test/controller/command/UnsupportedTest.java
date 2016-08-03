package src.test.controller.command;

import src.view.*;
import org.junit.Before;
import org.junit.Test;
import src.controller.command.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class UnsupportedTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        command = new UnsupportedCommand(view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProceed = command.canProcess("ifDontMatchAnyCommand");
        assertTrue(canProceed);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("ifDontMatchAnyCommand");
        verify(view).write("non-existent request: 'ifDontMatchAnyCommand'");
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