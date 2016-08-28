package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class ClearTable extends Command {

    private final DatabaseManager manager;
    private final View view;

    public ClearTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear ");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\s+");
        if (data.length != 2) {
            throw new IllegalArgumentException("Expected format is 'clear <table>'. But actual '" + command + "'");
        }
        manager.clear(data[1]);
        view.write(String.format("Table '%s' cleared", data[1]));
    }

    @Override
    public String commandFormat() {
        return "clear <>";
    }

    @Override
    public String description() {
        return "\t\tClear data of <table>";
    }
}




