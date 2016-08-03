package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class CreateTable extends Command {

    public CreateTable(DatabaseManager manager, View view) {
        super(manager, view);
    }

    String orExit = " or '0' to main menu.";
    String fromLetter = "(the name must begin with a letter!)";
    private boolean exitMain;
    String query = "";

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
            create.process("create|" + table); // красивый вывод новосозданной таблички
        } else {
            view.write("Main menu:");
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

            if (input.equals("0")) {
                exit = true;
                exitMain = true;
            } else if (input.equals("")) {
                view.write("Enter the name for a new table, but you enter an empty string.");
            } else {
                if (checkNameStartWithLetterB(input)) {
                    query += input + "(";
                    view.write("Name for a new table: " + input);
                    exit = true;
                }
            }
        }
    }

    private void createColumnPrimaryKey() {
        boolean exit = false;
        while (!exit) {
            view.write("Enter name to column of PRIMARY KEY" + fromLetter + orExit);
            String input = view.read();
            if (input.equals("0")) {
                exit = true;
                exitMain = true;
            } else if (input.equals("")) {
                view.write("Enter name to column of PRIMARY KEY, but not an empty string!");
            } else {
                if (checkNameStartWithLetterB(input)) {
                    view.write("Column name of PRIMARY KEY: " + input);
                    query += input + " SERIAL NOT NULL PRIMARY KEY";
                    exit = true;
                }
            }
        }
    }

    private void createColumn() {
        boolean exit = false;
        while (!exit) {
            view.write("Enter the name of the next column" + fromLetter + " or '5' to finish creating" + orExit);
            String input = view.read();
            if (input.equals("5")) {
                query += ")";
                exit = true;
            } else if (input.equals("0")) {
                exit = true;
                exitMain = true;
            } else if (input.equals("")) {
                view.write("Enter the column name, but you enter an empty string");
            } else {
                if (checkNameStartWithLetterB(input)) {
                    query += "," + input + " varchar(225)";
                    view.write("The name of the next column " + "'" + input +"'");
                    createColumn();
                    exit = true;
                }
            }
        }
    }

    public boolean checkNameStartWithLetterB(String input) {
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
