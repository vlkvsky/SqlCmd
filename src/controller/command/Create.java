package src.controller.command;

import src.model.DataSet;
import src.model.DatabaseManager;
import src.view.View;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class Create implements Command {
    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {

        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String [] data = command.split("[|]");
        if (data.length %2 != 0){
            throw new IllegalArgumentException(
                    "Must be an even number of parameters. " + //парные числа
                            "Example: 'create|tableName|column1|value1|column2|value2|...|columnN|valueN'. " +
                            "But actual: '" + command + "'");
        }

        DataSet dataSet = new DataSet();
        String tableName = data[1];

        for (int index = 1; index < data.length / 2; index++) {
            String columnName = data[index*2];
            String value = data[index*2 + 1];
            dataSet.put(columnName,value);
        }
        manager.create(tableName, dataSet);
        view.write(String.format("Value '%s' added to table '%s'", dataSet,tableName));

    }
}
