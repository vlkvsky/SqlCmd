package test.model;

import src.model.DatabaseManager;
import src.model.InMemoryDataBaseManager;
import test.model.DatabaseManagerTest;

public class InMemoryDataBaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDataBaseManager() {
        return new InMemoryDataBaseManager();
    }
}
