package src.controller.command;

import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Exit implements Command {

    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        view.write("See you later!");
        throw new ExitException();
    }
}

