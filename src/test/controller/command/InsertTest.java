package src.test.controller.command;

import src.view.*;
import org.junit.Before;
import org.junit.Test;
import src.controller.command.*;
import src.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by serzh on 6/7/16.
 */
public class InsertTest {

    private Command command;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        View view = mock(View.class);
        command = new Insert(manager, view);
    }

    @Test
    public void testCanProcess() throws Exception {
        boolean canProcess = command.canProcess("insert|tableName");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithoutParameters() throws Exception {
        boolean canProcess = command.canProcess("insert|tableName");
        assertTrue(canProcess);
    }

    @Test
    public void testProcessWithWrongCommand() throws Exception {
        boolean canProcess = command.canProcess("insert34");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessFindWithOnlySlash() {
        try {
            command.process("insert|");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'insert|<>', but expected: insert|", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThan2() {
        try {
            command.process("insert|tableName|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'insert|<>', but expected: insert|tableName|qwe", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThan2() {
        try {
            command.process("insert");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Format 'insert|<>', but expected: insert", e.getMessage());
        }
    }
}