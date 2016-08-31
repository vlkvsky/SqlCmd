package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class Connect extends Command {

    public Connect(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        String databaseName = configuration.getDbName();
        String userName = configuration.getUsername();
        String password = configuration.getPassword();
        if (input.toLowerCase().equals("connect")) {
            try {
                manager.connect(databaseName, userName, password);
            } catch (Exception e) {
                view.write("Can not get connection to default DB. Configuration of 'configuration/SqlCmd.properties' is incorrect.");
                return;
            }
        } else {
            validationParameters(input);
            String[] data = input.split("\\s+");
            databaseName = data[1];
            userName = data[2];
            password = data[3];
            manager.connect(databaseName, userName, password);
        }
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
