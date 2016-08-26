package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class Connect extends Command {

    public Connect(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        validationParameters(input);
        String[] data = input.split("\\s+");
        String databaseName = data[1];
        String userName = data[2];
        String password = data[3];

        manager.connect(databaseName, userName, password);
        view.write("Connection successful. To see the available commands, type <help>");
    }

    @Override
    public String commandFormat() {
        return "connect DB user password";
    }

    @Override
    public String description() {
        return "Connect to DB";
    }

}
