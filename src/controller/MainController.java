package src.controller;

import src.controller.command.*;
import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public class MainController {

    private Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager ){
        this.view = view;
        this.commands = new Command[] {
                new Connect(manager,view),
                new Help(view),
                new Exit(view),
                new isConnected(manager,view),

                new List(manager,view),
                new Clear(manager,view),
                new Create(manager,view),
                new Find(manager,view),

                new UnsupportedCommand(view)};

    }

    public void run() {
               try {
            doWork();

        } catch (ExitException e){
            //do nothing
        }
    }

    private void doWork () {
        view.write("Hello user!");
        view.write("Enter DB name, login, password in the format: connect|sqlcmd|vlkvsky|0990");
        while (true) {
            String input = view.read();

            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (Exception e) {
                    if (e instanceof ExitException) {
                        throw e;
                    }
                    printError(e);
                    break;
                }
            }
            view.write("Enter the command or 'help'");

        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null){
            message += " " + e.getCause().getMessage();
        }
        view.write("Can't perform the action! Problem: " + message);
        view.write("Repeat one more time:");
    }
}

