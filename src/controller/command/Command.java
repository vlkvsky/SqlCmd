package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

public abstract class Command {

    DatabaseManager manager;
    View view;

    Command(DatabaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    Command(View view) {
        this.view = view;
    }

    Command() {
    }

    public abstract void process(String input);

    public abstract String commandFormat();


    public abstract String description();

    public boolean canProcess(String input) {
        String[] parametersCommandFormat = splitInput(commandFormat());
        String[] parametersInput = splitInput(input);
        return parametersInput[0].toLowerCase().equals(parametersCommandFormat[0].toLowerCase());
    }

    boolean deleteConfirmation(String name) {
        view.write(String.format("Are you sure you want to delete '%s'? Y/N", name));
        if (view.read().toUpperCase().equals("Y")) {
            return false;
        }
        view.write("Action canceled.");
        return true;
    }

    void validationParameters(String input) {
        int formatLength = parametersLength(commandFormat());
        int inputLength = parametersLength(input);
        if (formatLength != inputLength) {
            throw new IllegalArgumentException(String.format("Format '%s', but expected: %s", commandFormat(), input));
        }
    }

    void checkNameStartWithLetter(String input) {
        char fistChar = input.charAt(0);
        if (!(fistChar >= 'a' && fistChar <= 'z') && !(fistChar >= 'A' && fistChar <= 'Z')) {
            throw new IllegalArgumentException(String.format(
                    "Name of DB must start with a letter, but expected '%s'", fistChar));
        }
    }

    String[] splitInput(String input) {
        return input.split("\\|");
    }

    private int parametersLength(String input) {
        return input.split("\\|").length;
    }
}