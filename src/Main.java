package src;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import src.controller.MainController;
import src.model.DatabaseManager;
import src.model.PostgresManager;
import src.view.Console;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public class Main {
    public static void main(String[] args) {

        Logger.getRootLogger().setLevel(Level.OFF); //Disable log4j from text table formatter
        View view = new Console();
        DatabaseManager manager = new PostgresManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
