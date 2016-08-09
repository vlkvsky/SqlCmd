package src.test.controller.command;

import src.view.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.controller.command.*;

import static org.junit.Assert.*;

public class ExitTest {
    private View view = Mockito.mock(View.class);
    private Command command;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        boolean canProcess = command.canProcess("exit");
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessQweString() {
        boolean canNotProcess = command.canProcess("qwe");
        assertFalse(canNotProcess);
    }

    @Test
    public void testProcessExitCommand_throwsExitException() {
        try {
            command.process("exit");
            fail("Expected ExitException");
        } catch (ExitException e) {
            // do nothing
        }
        Mockito.verify(view).write("See you later!");
    }

    @Test(expected = ExitException.class)
    public void classExitException() {
        command.process("exit");
    }
}
