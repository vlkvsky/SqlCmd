package src.controller.command;


import src.model.DatabaseManager;
import src.view.View;

public class DeleteTable extends Command {

    public DeleteTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String tableName = splitInput(input)[1];

        if (!dropConfirmation(tableName)) return;
        manager.dropTable(tableName);
        view.write(String.format("Table '%s' successfully deleted.", tableName));
    }

    @Override
    public String commandFormat() {
        return "deleteTable|tableName";
    }

    @Override
    public String description() {
        return "\t\t\tDelete 'tableName'";
    }
}
