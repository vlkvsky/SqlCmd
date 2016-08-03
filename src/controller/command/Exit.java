package src.controller.command;

import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Exit extends Command {

    public Exit(View view) {
        super(view);
    }

    @Override
    public void process(String command) {
        view.write("See you later!");
        throw new ExitException();
    }

    @Override
    public String commandFormat() {
        return "exit";
    }

    @Override
    public String description() {
        return "\t\t\tClose application";
    }

    public static class ex extends Exit {
        public ex(View view) {
            super(view);
        }

        @Override
        public void process(String command) {
            view.write("See you later!");
            throw new ExitException();
        }
        @Override
        public String commandFormat() {
            return "ex";
        }

    }
}
