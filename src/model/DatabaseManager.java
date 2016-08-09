package src.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {

    List<Map<String, Object>> getTableData(String tableName);

    Set<String> getDatabases();

    Set<String> getTableNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void createDB(String databaseName);

    void deleteDB(String databaseName);

    void createTable(String query);

    void deleteTable(String query);

    void insert(String tableName, Map<String, Object> input);

    void update(String tableName, int id, Map<String, Object> newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    String getUser();

    String getPassword();

    String getDatabaseName();

    int getTableSize(String tableName);

    void deleteTableRow(String tableName, String id);


}
