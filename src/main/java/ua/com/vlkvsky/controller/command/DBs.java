package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import java.util.Set;

public class DBs extends Command {

    public DBs(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        Set<String> set = manager.getDatabases();
        if (set.size() > 0) {
            String bases = set.toString();
            String result = bases.substring(1, bases.length() - 1);
            view.write(String.format("Existing DataBases: %s", result));
        } else {
            view.write("There is no DataBases");
        }
    }

    @Override
    public String commandFormat() {
        return "DBs";
    }

    @Override
    public String description() {
        return "Get all DataBases";
    }
}
