package project;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import project.model.DatabaseManager;
import project.view.Console;
import project.view.View;
import project.controller.MainController;
import project.model.PostgresManager;

public class Main {
    public static void main(String[] args) {

        Logger.getRootLogger().setLevel(Level.OFF); //Disable log4j from text table formatter
        View view = new Console();
        DatabaseManager manager = new PostgresManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
