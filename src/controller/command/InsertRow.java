package src.controller.command;

import src.model.DatabaseManager;
import src.model.TableConstructor;
import src.view.View;

import java.util.*;

public class InsertRow extends Command {

    private Set<String> columns;
    private boolean exitMain;
    public InsertRow(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        exitMain = false;
        String[] data = splitInput(input);
        validationParameters(input);
        String tableName = data[1];
        columns = manager.getTableColumns(tableName);
        Map<String, Object> command = createQuery();
        if (exitMain) {
            view.write("Main menu:");
        } else {
            manager.insert(tableName, command);
            view.write(String.format("Successfully added to the table '%s' this data:", tableName));
            view.write(getTableConstructor(command));
        }
    }

    private Map<String, Object> createQuery() {
        Map<String, Object> data = new LinkedHashMap<>();
        for (String column : columns) {
            Object value = setValue(column);
            if (value.equals("0")) {
                exitMain = true;
                return data;
            } else {
                data.put(column, value);
            }
        }
        return data;
    }

    private Object setValue(String column) {
        boolean exit = false;
        Object input = "";
        while (!exit) {
            view.write(String.format("Enter a value in the field '%s' or enter '0' to exit to the main menu .", column));
            input = view.read();
            if (input.equals("")) {
                view.write("Enter name to column of PRIMARY KEY, but not an empty string!");
            } else {
                exit = true;
            }
        }
        return input;
    }

    private String getTableConstructor(Map<String, Object> dataSet) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.putAll(dataSet);

        List<Map<String, Object>> tableData = new LinkedList<>();
        tableData.add(map);
        TableConstructor constructor = new TableConstructor(dataSet.keySet(), tableData);
        return constructor.getTableString();
    }

    @Override
    public String commandFormat() {
        return "insert|<>";
    }

    @Override
    public String description() {
        return "\tAdd data to <table>";
    }

}
