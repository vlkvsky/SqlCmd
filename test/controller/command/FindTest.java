package test.controller.command;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import src.controller.command.Command;
import src.controller.command.Find;
import src.model.DataSet;
import src.model.DatabaseManager;
import src.view.View;

import static junit.framework.TestCase.assertEquals;


/**
 * Created by Вадим Сергеевич on 01.07.2016.
 */
public class FindTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup(){
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Find(manager, view);
    }

    @Test
    public void testPrintTableData(){
        //given
        when(manager.getTableColumns("t1"))
                .thenReturn(new String[] {"id", "name", "password"});

        DataSet user1 = new DataSet();
        user1.put("id", 12);
        user1.put("name", "Vadym");
        user1.put("password", "*****");

        DataSet user2 = new DataSet();
        user2.put("id", 13);
        user2.put("name", "Nastya");
        user2.put("password", "+++++");

        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getTableData("t1"))
                .thenReturn(data);

        //when
        command.process("find|t1");

        //then
        shouldPrint("[------------------, " +
                    "|id|name|password|, " +
                    "------------------, " +
                    "|12|Vadym|*****|, " +
                    "|13|Nastya|+++++|, " +
                    "------------------]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected,
                captor.getAllValues().toString());
    }

    @Test
    public void TestCanProcessFindWithParametersString(){
        // when
        boolean canProcess = command.canProcess("find|t1");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessWithoutParametersString(){
        // when
        boolean canProcess = command.canProcess("find");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void TestCantProcessFindQweString(){
        // when
        boolean canProcess = command.canProcess("qwe|t1");
        // then
        assertFalse(canProcess);
    }

    @Test
    public void testPrintEmptyTableData(){
        //given
        when(manager.getTableColumns("t1"))
                .thenReturn(new String[] {"id", "name", "password"});

        DataSet[] data = new DataSet[0];
        when(manager.getTableData("t1"))
                .thenReturn(data);
        //when
        command.process("find|t1");

        //then
        shouldPrint(
                        "[------------------, " +
                        "|id|name|password|, " +
                        "------------------, " +
                        "------------------]");
    }

    @Test
    public void testPrintTableWithOneColumn(){
        //given
        when(manager.getTableColumns("testTable"))
                .thenReturn(new String[] {"name"});

        DataSet user1 = new DataSet();
        user1.put("name", "Vadym");

        DataSet user2 = new DataSet();
        user2.put("name", "Nastya");

        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getTableData("testTable"))
                .thenReturn(data);

        //when
        command.process("find|testTable");

        //then
        shouldPrint("[------------------, " +
                "|name|, " +
                "------------------, " +
                "|Vadym|, " +
                "|Nastya|, " +
                "------------------]");
    }

}
