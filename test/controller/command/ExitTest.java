package test.controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import src.controller.command.Command;
import src.controller.command.Exit;
import src.controller.command.ExitException;
import src.view.View;

import static org.junit.Assert.*;

/**
 * Created by Вадим Сергеевич on 01.07.2016.
 */
public class ExitTest {
    private View view = Mockito.mock(View.class);

    @Test
    public void TestCanProcessExitString(){
        // given
        Command command = new Exit(view);

        // when
        boolean canProcess = command.canProcess("exit");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessQweString(){
        // given
        Command command = new Exit(view);

        // when
        boolean canProcess = command.canProcess("qwe");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void TestProcessExitCommand_throwsExitException(){
        // given
        Command command = new Exit(view);

        // when
        try {
            command.process("exit");
            fail("Expected ExitException");
        }
        catch (ExitException e ){
            //do nothing
        }

        // then
        Mockito.verify(view).write("See you later!");
    }

}
