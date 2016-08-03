package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
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
        return "\t\tGet all tables of DB";
    }
}
