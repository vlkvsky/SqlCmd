package ua.com.vlkvsky;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.com.vlkvsky.controller.MainController;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.model.PostgresManager;
import ua.com.vlkvsky.view.Console;
import ua.com.vlkvsky.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Logger.getRootLogger().setLevel(Level.OFF); //Disable log4j from text table formatter
        View view = new Console();
        DatabaseManager manager = new PostgresManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
