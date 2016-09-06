package ua.com.vlkvsky.controller;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.com.vlkvsky.controller.command.*;
import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MainController {

    private final List<Command> commands;
    private final View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        Logger.getRootLogger().setLevel(Level.OFF); //Disable log4j from text table formatter
        this.commands = new ArrayList<>(Arrays.asList(
                new Exit.ex(view),                   // выход
                new Exit(view),                      // выход
                new Connect(manager, view),          // подключение
                new isConnected(manager, view),      // проверка подключения

                new Help(view),                      // возможные комманды
                new DBs(manager, view),              // вывод существующих баз данных
                new Tables(manager, view),           // вывод существующих таблиц
                new ContentTable(manager, view),     // содержимое таблицы
                new CreateTable(manager, view),      // создать таблицу
                new CreateDB(manager, view),         // создать базу данных
                new InsertRow(manager, view),        // вставить данные в таблицу
                new DeleteTable(manager, view),      // удалить таблицу
                new DeleteDB(manager, view),         // удалить базу данных
                new DeleteRow(manager, view),        // удалить строку из таблицы
                new ClearTable(manager, view),       // очистить таблицу

                new UnsupportedCommand(view)         // несуществующая комманда
        ));
    }

    public void run() {
        try {
            doWork();
        } catch (ExitException e) {
            //do nothing
        }
    }

    private void doWork() {
        view.write("Welcome " + System.getProperty("user.name") + "!");
        view.write("Enter DB name, login, password in the format: connect DATABASE USER PASSWORD");

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
            view.write("-----------------" +
                    "\nEnter the command:");
        }
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += ". " + e.getCause().getMessage();
        }
        view.write("Can't perform the action! Problem: " + message);
        view.write("Repeat one more time.");
    }

    public static class Configuration {

        public static final String CONFIGURATION_PROPERTIES = "configuration/sqlcmd.properties";
        private Properties properties;

        public Configuration() {
            FileInputStream fileInput = null;
            properties = new Properties();
            File file = new File(CONFIGURATION_PROPERTIES);
            try {
                fileInput = new FileInputStream(file);
                properties.load(fileInput);
            } catch (Exception e) {
                System.out.println("Error loading config " + file.getAbsolutePath());
                e.printStackTrace();
            } finally {
                if (fileInput != null) {
                    try {
                        fileInput.close();
                    } catch (IOException e) {
                        // do nothing;
                    }
                }
            }
        }

        public String getDbHost() {
            return properties.getProperty("db.host");
        }

        public String getDbPort() {
            return properties.getProperty("db.port");
        }

        public String getDbDriver() {
            return properties.getProperty("jdbc.driver");
        }

        public String getDbName() {
            return properties.getProperty("db.name");
        }

        public String getUsername() {
            return properties.getProperty("db.username");
        }

        public String getTestDb() {
            return properties.getProperty("db.test");
        }

        public String getPassword() {
            return properties.getProperty("db.password");
        }
    }
}

