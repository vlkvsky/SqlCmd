package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class ClearTable extends Command {

    private DatabaseManager manager;
    private View view;

    public ClearTable(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Expected format is 'clear|tableName'. But actual '" + command +"'");
        }
        manager.clear(data[1]);
        view.write(String.format("Table '%s' cleared",data[1]));
    }

    @Override
    public String commandFormat() {
        return "clear|tableName";
    }

    @Override
    public String description() {
        return "ClearTable data of the table";
    }
}




