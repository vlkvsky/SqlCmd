package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.TableConstructor.Columns;
import ua.com.vlkvsky.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help extends Command {

    private final View view;
    private final List<Command> commands;

    public Help(View view) {
        this.view = view;
        commands = new ArrayList<>(Arrays.asList(
                this,
                new Connect(manager, view),
                new DBs(manager, view),
                new Tables(manager, view),
                new CreateDB(manager, view),
                new CreateTable(manager, view),
                new ContentTable(manager, view),
                new InsertRow(manager, view),
                new DeleteRow(manager, view),
                new ClearTable(manager, view),
                new DeleteTable(manager, view),
                new DeleteDB(manager, view),
                new Exit(view)
        ));
    }

    @Override
    public void process(String input) {
        view.write("Available commands:\n" +
                "+--------------------------" + "+" + "-----------------------------+");
        Columns columns = new Columns();
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            columns.addLine("|", command.commandFormat(), "|", command.description(), "|");
            columns.addLine("|", "--------------------------", "+", "-----------------------------", "|");
        }
        view.write(columns.print());
    }

    @Override
    public String commandFormat() {
        return "help";
    }

    @Override
    public String description() {
        return "Get available commands";
    }
}