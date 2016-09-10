package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class ClearTable extends Command {

    public ClearTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\s+");
        if (data.length != 2) {
            throw new IllegalArgumentException("Expected format is 'clear <table>'. But actual '" + command + "'");
        }
        if (deleteConfirmation(data[1])) return;
        manager.clear(data[1]);
        view.write(String.format("Table '%s' cleared", data[1]));
    }

    @Override
    public String commandFormat() {
        return "clear <>";
    }

    @Override
    public String description() {
        return "Clear data of <table>";
    }
}




