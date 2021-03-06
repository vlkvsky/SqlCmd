package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import java.util.Set;

public class Tables extends Command {

    public Tables(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        Set<String> set = manager.getTableNames();
        if (set.size() > 0) {
            String tables = set.toString();
            String result = tables.substring(1, tables.length() - 1);
            view.write(String.format("Available tables: %s", result));
        } else {
            view.write("DB is empty.");
        }
    }

    @Override
    public String commandFormat() {
        return "tables";
    }

    @Override
    public String description() {
        return "Get all tables of DB";
    }
}
