package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public abstract class Command {

    protected DatabaseManager manager;
    protected View view;

    public Command(DatabaseManager manager, View view) {
        this.view = view;
        this.manager = manager;
    }

    public Command(View view) {
        this.view = view;
    }

    public Command() {
    }

    public abstract void process(String input);

    public abstract String commandFormat();


    public abstract String description();

    public boolean canProcess(String input) {
        String[] parametersCommandFormat = splitInput(commandFormat());
        String[] parametersInput = splitInput(input);
        return parametersInput[0].toLowerCase().equals(parametersCommandFormat[0].toLowerCase());
    }

    public boolean deleteConfirmation(String name) {
        view.write(String.format("Are you sure you want to delete '%s'? Y/N", name));
        if (view.read().toUpperCase().equals("Y")) {
            return true;
        }
        view.write("Action canceled.");
        return false;
    }

    public void validationParameters(String input) {
        int formatLength = parametersLength(commandFormat());
        int inputLength = parametersLength(input);
        if (formatLength != inputLength) {
            throw new IllegalArgumentException(String.format("Format '%s', but expected: %s", commandFormat(), input));
        }
    }

    public void validationPairParameters(String input) {
        int inputLength = parametersLength(input);
        if (inputLength % 2 != 0) {
            throw new IllegalArgumentException(String.format("Must be an even number of parameters, " +
                    "format '%s', but expected: '%s'", commandFormat(), input));
        }
    }

    public void checkNameStartWithLetter(String input, String name) {
        char fistChar = input.charAt(0);
        if (!(fistChar >= 'a' && fistChar <= 'z') && !(fistChar >= 'A' && fistChar <= 'Z')) {
            throw new IllegalArgumentException(String.format(
                    "Name %s must start with a letter, but expected '%s'", name, fistChar));
        }
    }

    public String[] splitInput(String input) {
        return input.split("\\|");
    }

    public int parametersLength(String input) {
        return input.split("\\|").length;
    }
}