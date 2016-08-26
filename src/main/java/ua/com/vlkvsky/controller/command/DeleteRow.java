package ua.com.vlkvsky.controller.command;


import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class DeleteRow extends Command {

    public DeleteRow(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String[] RowToDelete = input.split("\\s+");

        String databaseName = RowToDelete[1];
        String id = RowToDelete[2];

        if (deleteConfirmation(RowToDelete[2])) return;

        manager.deleteTableRow(databaseName, id);
        view.write(String.format("Row with id '%s' successfully deleted.", RowToDelete[2]));


    }

    @Override
    public String commandFormat() {
        return "deleteRow <> id";
    }

    @Override
    public String description() {
        return "Delete row <id> from <table>";
    }
}
