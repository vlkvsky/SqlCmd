package ua.com.vlkvsky.controller.command;

import ua.com.vlkvsky.view.View;

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
        return "Close application";
    }

    public static class ex extends Exit {
        public ex(View view) {
            super(view);
        }

        @Override
        public String commandFormat() {
            return "ex";
        }
    }
}
