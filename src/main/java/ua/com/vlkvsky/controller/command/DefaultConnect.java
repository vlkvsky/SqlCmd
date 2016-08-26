package ua.com.vlkvsky.controller.command;
import ua.com.vlkvsky.Configuration;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.model.PostgresManager;
import ua.com.vlkvsky.view.Console;
import ua.com.vlkvsky.view.View;

import java.sql.SQLException;


public class DefaultConnect {
    DatabaseManager manager = new PostgresManager();
    View view = new Console();
    public void process() {
        Configuration configuration = new Configuration();

        try{
            String databaseName = configuration.getDbName();
            String userName = configuration.getUsername();
            String password = configuration.getPassword();
            manager.connect(databaseName, userName, password);
            view.write("Connection to default DB successful. To see the available commands, type <help>");
        } catch (Exception e){
            view.write("Can not get connection to default DB. Configuration of 'configuration/SqlCmd.properties' is incorrect.");
        }
    }

}
