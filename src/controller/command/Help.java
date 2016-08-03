package src.controller.command;

import src.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help extends Command {

    private View view;
    private List<Command> commands;


    public Help(View view) {
        this.view = view;
        commands = new ArrayList<>(Arrays.asList(
                this,
//                new Connect(manager, view),
                new DBs(manager, view),
                new Tables(manager, view),
                new CreateDB(manager, view),
                new CreateTable(manager, view),
                new ContentTable(manager, view),
                new Insert(manager, view),
                new ClearTable(manager, view),
                new DeleteTable(manager, view),
                new DeleteDB(manager, view),
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