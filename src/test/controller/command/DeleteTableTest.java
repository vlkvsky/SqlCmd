package src.test.controller.command;

import src.view.*;
import org.junit.Before;
import org.junit.Test;
import src.controller.command.*;
import src.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeleteTableTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DeleteTable(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("deleteTable|");
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        boolean canNotProcess = command.canProcess("deleteTables|");
        assertFalse(canNotProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        boolean canProcess = command.canProcess("deleteTable");
        assertTrue(canProcess);
    }

    @Test
    public void testProcess() throws Exception {
        when(view.read()).thenReturn("y");
        command.process("deleteTable|nameTable");
        verify(view).write("Are you sure you want to delete 'nameTable'? Y/N");
        verify(manager).deleteTable("nameTable");
        verify(view).write("Table 'nameTable' successfully deleted.");
    }

    @Test
    public void testActionCanceled() throws Exception {
        when(view.read()).thenReturn("n");
        command.process("deleteTable|nameTable");
        verify(view).write("Are you sure you want to delete 'nameTable'? Y/N");
        verify(view).write("Action canceled.");
    }

    @Test
    public void testProcessWrongFormat() throws Exception {
        try {
            command.process("createTable|tableName|wrong");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'deleteTable|<>', but expected: createTable|tableName|wrong", e.getMessage());
        }
    }
}