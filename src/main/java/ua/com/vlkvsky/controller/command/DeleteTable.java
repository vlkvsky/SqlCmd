package ua.com.vlkvsky.controller.command;


import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class DeleteTable extends Command {

    public DeleteTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String tableName = splitInput(input)[1];

        if (deleteConfirmation(tableName)) return;
        manager.deleteTable(tableName);
        view.write(String.format("Table '%s' successfully deleted.", tableName));
    }

    @Override
    public String commandFormat() {
        return "deleteTable|<>";
    }

    @Override
    public String description() {
        return "Delete <table>";
    }
}
