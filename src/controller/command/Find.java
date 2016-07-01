package src.controller.command;

import src.model.DataSet;
import src.model.DatabaseManager;
import src.view.View;

import java.util.Arrays;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Find implements Command {
    private DatabaseManager manager;
    private View view;

    public Find(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1]; // TODO to add validation

        String[] tableColumns = manager.getTableColumns(tableName);
        printHeader(tableColumns);

        DataSet[] tableData = manager.getTableData(tableName);
        printTable(tableData);
    }


    private void printTable(DataSet[] tableData) {
        for (DataSet row : tableData) {
            printRow(row);
        }
        view.write("------------------");
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value : values){
            result += value + "|";
        }
        view.write(result);

    }

    private void printHeader(String[] tableColumns) {

        String result = "|";
        for (String name : tableColumns){
            result += name + "|";
        }
        view.write("------------------");
        view.write(result);
        view.write("------------------");
    }

}

