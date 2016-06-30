package src.controller.command;

import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class UnsupportedCommand implements Command {
    private View view;

    public UnsupportedCommand(View view) {
        this.view = view;
    }
    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
        view.write("non-existent request: " + "'" + command + "'");
    }
}
