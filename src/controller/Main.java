package src.controller;

import src.model.DatabaseManager;
import src.model.JDBCDatabaseManager;
import src.view.Console;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public class Main {
    public static void main(String[] args) {

        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
