package ua.com.vlkvsky.controller.command;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class DefaultConnect extends Command {

    public DefaultConnect(DatabaseManager manager, View view) {
        super(manager, view);
    }


    @Override
    public void process(String input) {
        validationParameters(input);

        try{
            String[] data = input.split("\\|");
            String databaseName = configuration.getDbName();
            String userName = configuration.getUsername();
            String password = configuration.getPassword();
            manager.connect(databaseName, userName, password);
            view.write("Connection to default DB successful. To see the available commands, type <help>");
        } catch (Exception e){
            view.write("Can not get connection to default DB. Configuration of 'configuration/SqlCmd.properties' is incorrect.");
        }
    }

    @Override
    public String commandFormat() {
        return "connect default";
    }

    @Override
    public String description() {
        return "Connect to default DB. Setting in '/configuration/sqlcmd.properties";
    }

}
