package src.test.controller.command;

import org.junit.Before;
import org.junit.Test;
import src.controller.command.Command;
import src.controller.command.CreateDB;
import src.model.DatabaseManager;
import src.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateDBTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new CreateDB(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("createDB|databaseName");
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        boolean canProcess = command.canProcess("createDB34|databaseName");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() throws Exception {
        boolean canProcess = command.canProcess("createDB");
        assertTrue(canProcess);
    }

    @Test
    public void testProcess() throws Exception {
        command.process("createDB|databaseName");
        verify(manager).createDB("databaseName");
        verify(view).write("DB 'databaseName' created.");
    }

    @Test
    public void testProcessWrongFormat() throws Exception {
        try {
            command.process("createDB|databaseName|wrong");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'createDB|<>', but expected: createDB|databaseName|wrong", e.getMessage());
        }
    }

    @Test
    public void testNameStartWithNumber() throws Exception {
        try {
            command.process("createDB|12databaseName");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Name of DB must start with a letter, but expected '1'", e.getMessage());
        }
    }
}