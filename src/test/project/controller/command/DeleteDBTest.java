package project.controller.command;

import org.junit.Before;
import org.junit.Test;
import project.model.DatabaseManager;
import project.view.View;
//import Command;
//import DeleteDB;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeleteDBTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DeleteDB(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("deleteDB|db");
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        boolean canNotProcess = command.canProcess("deleteFB|db");
        assertFalse(canNotProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        boolean canProcess = command.canProcess("deleteDB");
        assertTrue(canProcess);
    }


    @Test
    public void testProcess() throws Exception {
        when(view.read()).thenReturn("y");
        command.process("dropDB|db");

        verify(view).write("Are you sure you want to delete 'db'? Y/N");
        verify(manager).deleteDB("db");
        verify(view).write("DB 'db' successfully deleted");
    }

    @Test
    public void testProcessUpperY() throws Exception {
        when(view.read()).thenReturn("Y");
        command.process("deleteDB|db");

        verify(view).write("Are you sure you want to delete 'db'? Y/N");
        verify(manager).deleteDB("db");
        verify(view).write("DB 'db' successfully deleted");
    }

    @Test
    public void testActionCanceled() throws Exception {
        when(view.read()).thenReturn("N");
        command.process("dropDB|db");

        verify(view).write("Are you sure you want to delete 'db'? Y/N");
        verify(view).write("Action canceled.");
    }

    @Test
    public void testProcessWrongFormat() throws Exception {
        try {
            command.process("dropDB|db|wrong");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'deleteDB|<>', but expected: dropDB|db|wrong", e.getMessage());
        }
    }

    @Test
    public void testDeleteDBwithoutConnect() throws Exception {
        when(view.read()).thenReturn("Y");
        when(manager.getDatabaseName()).thenReturn("currentDB");
        command.process("dropDB|currentDB");
        verify(view).write("Are you sure you want to delete 'currentDB'? Y/N");
        verify(view).write("User is connected to DB. Not allow now!");
        verify(view).write("User is connected to DB. Not allow now!");
    }
}