package project.controller.command;

import org.junit.Before;
import org.junit.Test;
import project.model.DatabaseManager;
import project.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

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