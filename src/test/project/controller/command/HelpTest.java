package project.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import project.view.View;

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
        verify(view).write("Available commands:");
    }

}