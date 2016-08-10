package src.controller.command;


import src.model.DatabaseManager;
import src.view.View;

public class DeleteRow extends Command {

    public DeleteRow(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String[] RowToDelete = input.split("\\|");

        String databaseName = RowToDelete[1];
        String id = RowToDelete[2];

        if (deleteConfirmation(RowToDelete[2])) return;

        manager.deleteTableRow(databaseName, id);
        view.write(String.format("Row with id '%s' successfully deleted.", RowToDelete[2]));


    }

    @Override
    public String commandFormat() {
        return "deleteRow|<>|id";
    }

    @Override
    public String description() {
        return "Delete row <id> from <table>";
    }
}
