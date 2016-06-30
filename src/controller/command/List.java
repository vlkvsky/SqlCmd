package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

import java.util.Arrays;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class List implements Command {
    private View view;
    private DatabaseManager manager;

     public List(DatabaseManager manager, View view) {
         this.manager = manager;
         this.view = view;
     }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {

        String[] tableNames = manager.getTableNames();

        String message = Arrays.toString(tableNames);

        view.write(message);
    }
}
