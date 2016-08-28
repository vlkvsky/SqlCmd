package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.model.DatabaseManager;
import ua.com.vlkvsky.view.View;

public class CreateTable extends Command {

    private final String orExit = " or '0' to main menu.";
    private final String fromLetter = "(the name must begin with a letter!)";
    private boolean exitMain;
    private String query = "";

    public CreateTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    @Override
    public void process(String input) {
        exitMain = false;
        query = "";
        validationParameters(input);
        createQuery();

        if (!exitMain) {
            manager.createTable(query);
            String table = query.split("\\(")[0];
            view.write(String.format("Table %s created.", table));
            Command create = new ContentTable(manager, view);
            create.process("create " + table); // красивый вывод новосозданной таблички
        } else {
            view.write(" ");
        }
    }

    private void createQuery() {
        if (!exitMain) {
            createTableName();
        }
        if (!exitMain) {
            createColumnPrimaryKey();
        }
        if (!exitMain) {
            createColumn();
        }
    }

    private void createTableName() {
        boolean exit = false;
        while (!exit) {
            view.write("Enter the name for a new table:" + fromLetter + orExit);
            String input = view.read();

            switch (input) {
                case "0":
                    exit = true;
                    exitMain = true;
                    break;
                case "":
                    view.write("Enter the name for a new table, but you enter an empty string.");
                    break;
                default:
                    if (checkNameStartWithLetterB(input)) {
                        query += input + "(";
                        view.write("Name for a new table: " + input);
                        exit = true;
                    }
                    break;
            }
        }
    }

    private void createColumnPrimaryKey() {
        boolean exit = false;
        while (!exit) {
            view.write("Enter name to column of PRIMARY KEY" + fromLetter + orExit);
            String input = view.read();
            switch (input) {
                case "0":
                    exit = true;
                    exitMain = true;
                    break;
                case "":
                    view.write("Enter name to column of PRIMARY KEY, but not an empty string!");
                    break;
                default:
                    if (checkNameStartWithLetterB(input)) {
                        view.write("Column name of PRIMARY KEY: " + input);
                        query += input + " SERIAL NOT NULL PRIMARY KEY";
                        exit = true;
                    }
                    break;
            }
        }
    }

    private void createColumn() {
        boolean exit = false;
        while (!exit) {
            view.write("Enter the name of the next column" + fromLetter + " or '5' to finish creating" + orExit);
            String input = view.read();
            switch (input) {
                case "5":
                    query += ")";
                    exit = true;
                    break;
                case "0":
                    exit = true;
                    exitMain = true;
                    break;
                case "":
                    view.write("Enter the column name, but you enter an empty string");
                    break;
                default:
                    if (checkNameStartWithLetterB(input)) {
                        query += "," + input + " varchar(225)";
                        view.write("The name of the next column " + "'" + input + "'");
                        createColumn();
                        exit = true;
                    }
                    break;
            }
        }
    }

    private boolean checkNameStartWithLetterB(String input) {
        char fistChar = input.charAt(0);
        if (!(fistChar >= 'a' && fistChar <= 'z') && !(fistChar >= 'A' && fistChar <= 'Z') &&
                (!(fistChar >= 'а' && fistChar <= 'я') && !(fistChar >= 'А' && fistChar <= 'Я'))) {
            view.write(String.format("The name must begin with a letter, but not with: '%s'", fistChar));
            return false;
        }
        return true;
    }

    @Override
    public String commandFormat() {
        return "create";
    }

    @Override
    public String description() {
        return "\t\tCreate table step-by-step";
    }
}
