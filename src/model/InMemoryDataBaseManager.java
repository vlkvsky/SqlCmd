package src.model;

import java.util.Arrays;

/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public class InMemoryDataBaseManager implements DatabaseManager {

    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public DataSet[] getTableData(String tableName) {
        validateTable(tableName);
        return Arrays.copyOf(data, freeIndex);
    }

    private void validateTable(String tableName) {
        if (!TABLE_NAME.equals(tableName)){
            throw new UnsupportedOperationException("Only for '" + TABLE_NAME + "' table, but you try to work with:" + tableName);
        }
    }

    @Override
    public String[] getTableNames() {

        return new String[] {TABLE_NAME};
    }

    @Override
    public void connect(String database, String userName, String password) {
//do nothing
    }

    @Override
    public void clear(String tableName) {
        validateTable(tableName);
        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTable(tableName);

        data[freeIndex] = input;
        freeIndex++;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        for (int index = 0; index < freeIndex; index++) {
            if(data[index].get("id").equals(id))
                data[index].updateFrom(newValue);
        }
    }

    @Override
    public String[] getTableColumns(String tableName) {
        return new String[]{"name", "password", "id"};
    }

    @Override
    public boolean isConnected() {
        return true;
    }

}
