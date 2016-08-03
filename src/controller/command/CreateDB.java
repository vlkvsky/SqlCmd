package src.controller.command;


import src.model.DatabaseManager;
import src.view.View;

public class CreateDB extends Command {

    public CreateDB(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String databaseName = input.split("\\|")[1];
        checkNameStartWithLetter(databaseName, "базы");

        manager.createDB(databaseName);
        view.write(String.format("DB '%s' created.", databaseName));
    }

    @Override
    public String commandFormat() {
        return "createDB|<>";
    }

    @Override
    public String description() {
        return "\tCreate DB";
    }
}
