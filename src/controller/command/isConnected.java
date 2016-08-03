package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class isConnected extends Command {
    private DatabaseManager manager;
    private View view;

    public isConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("Command '%s' isn't allow. " +
                "You must connect! ",command));
    }

    @Override
    public String commandFormat() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
