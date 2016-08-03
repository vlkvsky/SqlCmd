package src.test.controller.command;

import src.view.*;
import org.junit.Before;
import org.junit.Test;
import src.controller.command.*;
import src.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class CreateTableTest {

    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        View view = mock(View.class);
        command = new CreateTable(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("create");
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        boolean canProcess = command.canProcess("createTable34");
        assertFalse(canProcess);
    }
}