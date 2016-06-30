package src.model;

/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public interface DatabaseManager {

    String TABLE_NAME = "t1";
    String DB_NAME = "sqlcmd";
    String LOGIN = "vlkvsky";
    String PASSWORD = "0990";

    DataSet[] getTableData(String tableName);

    String[] getTableNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);

    String[] getTableColumns(String tableName);

    boolean isConnected();
}
