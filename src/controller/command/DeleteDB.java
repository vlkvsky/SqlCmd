package src.controller.command;


import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by serzh on 5/11/16.
 */
public class DeleteDB extends Command {

    public DeleteDB(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String databaseName = splitInput(input)[1];
        if (!deleteConfirmation(databaseName)) return;

        String databaseNameConnect = manager.getDatabaseName();
        if (databaseName.equals(databaseNameConnect)) {
            view.write("DB is connected. Not allow to delete!");
            view.write("Reconnect to another DB.");
            return;
        }

        manager.deleteDatabase(databaseName);
        view.write(String.format("DB '%s' successfully deleted", databaseName));
    }

    @Override
    public String commandFormat() {
        return "deleteDB|<>";
    }

    @Override
    public String description() {
        return "\tDelete <DB>";
    }
}
