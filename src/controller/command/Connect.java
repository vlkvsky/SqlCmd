package src.controller.command;

import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Connect implements Command {
    private static String SAMPLE_COMMAND = "connect|sqlcmd|vlkvsky|0990";
    private DatabaseManager manager;
    private View view;

    public Connect(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {

            String[] data = command.split("[|]");
            if(data.length != count()) {
                throw new IllegalArgumentException(String.format("Wrong number of separating characters '|'. " +
                        "Expected '%s', but actual %",
                        count(),data.length));
            }
            String databaseName = data[1];
            String userName = data[2];
            String password = data[3];

            manager.connect(databaseName, userName, password);
            view.write("Connection successful!");

    }

    private int count() {
        return SAMPLE_COMMAND.split("[|]").length;
    }


    //ПОКА РАБОТАЕТ
}
