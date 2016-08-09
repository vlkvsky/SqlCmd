package src.controller.command;

import src.view.View;


public class UnsupportedCommand extends Command {
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

    @Override
    public String commandFormat() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
