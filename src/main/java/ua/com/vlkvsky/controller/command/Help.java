package ua.com.vlkvsky.controller.command;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import ua.com.vlkvsky.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help extends Command {

    private final View view;
    private final List<Command> commands;
    private Table table;

    public Help(View view) {
        this.view = view;
        Logger.getRootLogger().setLevel(Level.OFF); //Disable log4j from text table formatter
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
        if (table == null) {
            buildHeader();
            buildRows();
        }
        view.write("Available commands:\n" + table.render());
    }

    private void buildHeader() {
        table = new Table(2, BorderStyle.CLASSIC, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        table.addCell("Command");
        table.addCell("Description");
    }

    private void buildRows() {
        for (Command command : commands) {
            table.addCell(command.commandFormat());
            table.addCell(command.description());
        }
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