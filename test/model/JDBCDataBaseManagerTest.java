package test.model;

import src.model.DatabaseManager;
import src.model.JDBCDatabaseManager;

public class JDBCDataBaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDataBaseManager() {
        return new JDBCDatabaseManager();
    }
}
