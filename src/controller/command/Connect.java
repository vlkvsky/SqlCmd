package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Connect extends Command {

    public Connect(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String[] data = input.split("\\|");
        String databaseName = data[1];
        String userName = data[2];
        String password = data[3];

        manager.connect(databaseName, userName, password);
        view.write("Connection successful!");
    }
    @Override
    public String commandFormat() {
        return "connect|databaseName|userName|password";
    }

    @Override
    public String description() {
        return "Connect to DB";
    }

}
