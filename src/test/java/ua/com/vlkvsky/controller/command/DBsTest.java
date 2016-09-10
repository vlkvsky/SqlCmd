package ua.com.vlkvsky.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DBsTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new DBs(manager, view);
    }

    @Test
    public void testPrintGetTableNames() {
        when(manager.getDatabases()).thenReturn(new HashSet<>(Arrays.asList("db1", "db2")));
        command.process("databases");
        shouldPrint("[Existing DataBases: db1, db2]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void canProcessListWithRightParameter() {
        boolean canProcess = command.canProcess("DBs");
        assertTrue(canProcess);
    }

    @Test
    public void canProcessListWithWrongParameter() {
        boolean canNotProcess = command.canProcess("ghkk");
        assertFalse(canNotProcess);
    }

    @Test
    public void canProcessListWithoutParameter() {
        boolean canNotProcess = command.canProcess("");
        assertFalse(canNotProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        when(manager.getTableNames()).thenReturn(new HashSet<>(Arrays.asList("user", "test")));
        command.process(("databases"));
        shouldPrint("[There is no DataBases]");
    }
}