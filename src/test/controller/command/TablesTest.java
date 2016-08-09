package src.test.controller.command;

import org.mockito.ArgumentCaptor;
import src.view.*;
import org.junit.Before;
import org.junit.Test;
import src.controller.command.*;
import src.model.*;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TablesTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Tables(manager, view);
    }

    @Test
    public void testPrintGetTableNames() {
        when(manager.getTableNames()).thenReturn(new HashSet<>(Arrays.asList("user", "test")));
        command.process("tables");
        shouldPrint("[Available tables: test, user]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void canProcessWithRightParameter() {
        boolean canProcess = command.canProcess("tables");
        assertTrue(canProcess);
    }

    @Test
    public void canProcessWithWrongParameter() {
        boolean canNotProcess = command.canProcess("ghkk");
        assertFalse(canNotProcess);
    }

    @Test
    public void canProcessWithoutParameter() {
        boolean canNotProcess = command.canProcess("");
        assertFalse(canNotProcess);
    }

    @Test
    public void testPrintEmptyTableData() {
        when(manager.getTableNames()).thenReturn(new HashSet<String>());
        command.process(("tables"));
        shouldPrint("[DB is empty.]");
    }
}
