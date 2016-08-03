package src.controller.command;

import src.model.TableConstructor;
import src.view.View;

import java.util.*;

public class Help extends Command {

    private View view;
    private List<Command> commands;


    public Help(View view) {
        this.view = view;
        String[] strs = { "abc","123","zzz" };
        List<String> list = Arrays.asList( strs );
        commands = new ArrayList<>(Arrays.asList(
                this,
//                new Connect(manager, view),
                // TODO delete DB
                new Tables(manager,view),
                new CreateTable(manager,view),
                new DeleteTable(manager,view),
                new ContentTable(manager,view),
                new Insert(manager, view),
                new ClearTable(manager, view),
                new Exit(view)
        ));
    }

    @Override
    public void process(String input) {
        view.write("Available commands:");

        for (Command command : commands) {
                    view.write("\t • " + command.commandFormat() + "\t\t\t\t" + command.description() +
                    "\n\t═════════════════════════════════");
        }
    }

    @Override
    public String commandFormat() {
        return "help";
    }

    @Override
    public String description() {
        return "\t\t\tGet available commands";
    }
}




//    public void process(String command) {
//        view.write("Available commands:");
//
//        view.write("\t connect|dataBaseName|userName|password");
//        view.write("\t\t Connect to DB 'dataBaseName'");
//
//        view.write("\t help");
//        view.write("\t\t Recieve list of all available commands");
//
//        view.write("\t find|tableName");
//        view.write("\t\t Recieve the contents of the table 'tableName'");
//
//        view.write("\t list");
//        view.write("\t\t Recieve list of all tables");
//
//        view.write("\t clear|tableName");
//        view.write("\t\t Delete all values of 'tableName'"); //TODO если случайно ввел, нужно переспросить
//
//        view.write("\t create|tableName|column1|value1|column2|value2|...|columnN|valueN");
//        view.write("\t\t CreateTable values to 'tableName'");
//
//        view.write("\t exit");
//        view.write("\t\t Close the connection");
//    }