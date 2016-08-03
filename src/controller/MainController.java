package src.controller;

import src.controller.command.*;
import src.model.DatabaseManager;
import src.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public class MainController {


    private List<Command> commands;
    private View view;

    public MainController(View view, DatabaseManager manager ){
        this.view = view;
        this.commands = new ArrayList<>(Arrays.asList(
                new Connect(manager,view),      // подключение
                new isConnected(manager,view),  // проверка подключения

                new Help(view),                 // возможные комманды
                new Tables(manager,view),       // вывод существующих таблиц
                new ContentTable(manager,view), // содержимое таблицы
                new CreateTable(manager,view),  // создать таблицу
                new DeleteTable(manager,view),  // удалить таблицу
                new Insert(manager, view),      // вставить данный в таблицу
                new ClearTable(manager,view),   // очистить таблицу


                new Exit(view),                 // выход
                new UnsupportedCommand(view)    // несуществующая комманда
        ));
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
            message += ". " + e.getCause().getMessage();
        }
        view.write("Can't perform the action! Problem: " + message);
        view.write("Repeat one more time:");
    }
}

