package ua.com.vlkvsky;

import ua.com.vlkvsky.controller.MainController;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.model.PostgresManager;
import ua.com.vlkvsky.view.Console;
import ua.com.vlkvsky.view.View;

public class Main {
    public static void main(String[] args) {

        View view = new Console();
        DatabaseManager manager = new PostgresManager();

        MainController controller = new MainController(view, manager);
        controller.run();
    }
}
