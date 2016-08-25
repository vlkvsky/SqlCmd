package project.controller.command;

import project.model.DatabaseManager;
import project.model.TableConstructor;
import project.view.View;

import java.util.Map;
import java.util.Set;


public class ContentTable extends Command {

    public ContentTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);

        String tableName = splitInput(input)[1];
        Set<String> tableColumns = manager.getTableColumns(tableName);

        if (tableColumns.size() > 0) {
            java.util.List<Map<String, Object>> tableData = manager.getTableData(tableName);
            TableConstructor constructor = new TableConstructor(tableColumns, tableData);
            view.write(constructor.getTableString());
        } else {
            view.write(String.format("Data of '%s' is empty", tableName));
        }
    }

    @Override
    public String commandFormat() {
        return "content|<>";
    }

    @Override
    public String description() {
        return "\tGet content of <table>";
    }
}
