package test.controller.command;

import org.junit.Before;
import org.junit.Test;
import src.controller.command.ClearTable;
import src.controller.command.Command;
import src.model.DatabaseManager;
import src.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClearTableTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup(){
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new ClearTable(manager, view);
    }

    @Test
    public void testClearTable(){
        //given

        //when
        command.process("clear|t1");

        //then
        verify(manager).clear("t1");
        verify(view).write("Table 't1' cleared");
    }


    @Test
    public void TestCanProcessClearWithParametersString(){
        // when
        boolean canProcess = command.canProcess("clear|t1");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessWithoutParametersString(){
        // when
        boolean canProcess = command.canProcess("clear");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void TestCantProcessClearQweString(){
        // when
        boolean canProcess = command.canProcess("qwe|t1");
        // then
        assertFalse(canProcess);
    }


}
