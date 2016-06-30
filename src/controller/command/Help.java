package src.controller.command;

import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Help implements Command {

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("Available commands:");

        view.write("\t connect|dataBaseName|userName|password");
        view.write("\t\t Connect to DB 'dataBaseName'");

        view.write("\t help");
        view.write("\t\t Recieve list of all available commands");

        view.write("\t find|tableName");
        view.write("\t\t Recieve the contents of the table 'tableName'");

        view.write("\t list");
        view.write("\t\t Recieve list of all tables");

        view.write("\t clear|tableName");
        view.write("\t\t Delete all values of 'tableName'"); //TODO если случайно ввел, нужно переспросить

        view.write("\t create|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\t Create values to 'tableName'");

        view.write("\t exit");
        view.write("\t\t Close the connection");
    }
}
