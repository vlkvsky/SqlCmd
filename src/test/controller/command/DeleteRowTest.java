package src.test.controller.command;

import org.junit.Before;
import org.junit.Test;
import src.controller.command.Command;
import src.controller.command.DeleteDB;
import src.controller.command.DeleteRow;
import src.model.DatabaseManager;
import src.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeleteRowTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DeleteRow(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("deleteRow|table|id");
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        boolean canNotProcess = command.canProcess("deleteRowMistake|table|id");
        assertFalse(canNotProcess);
    }

    @Test
    public void testCanProcessDeleteWithoutParametersString() throws Exception {
        boolean canNotProcess = !command.canProcess("deleteRow|");
        assertFalse(canNotProcess);
    }


    @Test
    public void testProcess() throws Exception {
        when(view.read()).thenReturn("y");
        command.process("deleteRow|table|id");

        verify(view).write("Are you sure you want to delete 'id'? Y/N");
        verify(manager).deleteTableRow("table","id");
        verify(view).write("Row with id 'id' successfully deleted.");
    }

    @Test
    public void testProcessUpperY() throws Exception {
        when(view.read()).thenReturn("Y");
        command.process("deleteRow|table|id");

        verify(view).write("Are you sure you want to delete 'id'? Y/N");
        verify(manager).deleteTableRow("table","id");
        verify(view).write("Row with id 'id' successfully deleted.");
    }

    @Test
    public void testActionCanceled() throws Exception {
        when(view.read()).thenReturn("N");
        command.process("deleteRow|table|id");

        verify(view).write("Are you sure you want to delete 'id'? Y/N");
        verify(view).write("Action canceled.");
    }

    @Test
    public void testProcessWrongFormat() throws Exception {
        try {
            command.process("deleteRow|<>|id|wrong");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'deleteRow|<>|id', but expected: deleteRow|<>|id|wrong", e.getMessage());
        }
    }

    @Test
    public void testDeleteRowWhenConnect() throws Exception {
        when(view.read()).thenReturn("Y");
        when(manager.getDatabaseName()).thenReturn("currentDB");
        command.process("deleteRow|currentDB|id");
        verify(view).write("Are you sure you want to delete 'id'? Y/N");
        verify(view).write("Row with id 'id' successfully deleted.");
    }
}