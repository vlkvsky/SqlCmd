package src.model;

import java.sql.*;
import java.util.*;

public class PostgresManager implements DatabaseManager {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverException("Not installed PostgreSQL JDBC driver.", e);
        }
    }

    private static final String ERROR = "It is impossible because: ";
    private static final String HOST = "localhost";
    private static final String PORT = "5432";

    private Connection connection;
    private String user;
    private String password;
    private String database;

    @Override
    public void connect(String database, String user, String password) {
        if (user != null && password != null) {
            this.user = user;
            this.password = password;
        }
        this.database = database;

        closeOpenedConnection();
        getConnection();
    }

    private void getConnection() {
        try {
            String url = String.format("jdbc:postgresql://%s:%s/%s", HOST, PORT, database);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            connection = null;
            throw new DatabaseManagerException(
                    String.format("Cant get connection for model:%s user:%s", database, user), e);
        }
    }

    private void closeOpenedConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseManagerException(ERROR, e);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getTableData(String tableName) {
        List<Map<String, Object>> result = new LinkedList<>();
        // try-with-resources statement ensures that each resource is closed at the end of the statement
        try (Statement stmt = connection.createStatement();
             ResultSet tableData = stmt.executeQuery("SELECT * FROM public." + tableName)) {
            ResultSetMetaData metaData = tableData.getMetaData();

            while (tableData.next()) {
                Map<String, Object> data = new LinkedHashMap<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    data.put(metaData.getColumnName(i), tableData.getObject(i));
                }
                result.add(data);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void createDB(String databaseName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + databaseName);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteDB(String databaseName) {
        try (Statement statement = connection.createStatement()) {
//            statement.executeUpdate("DROP DATABASE IF EXISTS " + databaseName + " CASCADE");
            statement.executeUpdate("DROP DATABASE " + databaseName);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tables = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement();
             ResultSet tableNames = stmt.executeQuery("SELECT table_name FROM information_schema.tables " +
                     "WHERE table_schema='public' AND table_type='BASE TABLE'")) {
            while (tableNames.next()) {
                tables.add(tableNames.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    @Override
    public void clear(String tableName) throws RuntimeException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM " + tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void insert(String tableName, Map<String, Object> input) {
        try (Statement stmt = connection.createStatement()) {
            String tableNames = getNameFormatted(input, "%s,");
            String values = getValuesFormatted(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ")" +
                    "VALUES (" + values + ")");
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void createTable(String table_name) {
        try (Statement statement = connection.createStatement()) {
//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table_name);
            statement.executeUpdate("CREATE TABLE " + table_name);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteTable(String table_name) {
        try (Statement statement = connection.createStatement()) {
//            statement.executeUpdate("DROP TABLE IF EXISTS " + table_name + " CASCADE");
            statement.executeUpdate("DROP TABLE " + table_name);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> columns = new LinkedHashSet<>();
        try (Statement stmt = connection.createStatement();
             ResultSet tableColumns = stmt.executeQuery("SELECT * FROM information_schema.columns WHERE " +
                     "table_schema = 'public' AND table_name = '" + tableName + "'")) {
            while (tableColumns.next()) {
                columns.add(tableColumns.getString("column_name"));
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    private String getNameFormatted(Map<String, Object> newValue, String format) {
        StringBuilder strings = new StringBuilder("");
        for (String name : newValue.keySet()) {
            strings.append(String.format(format, name));
        }
        return strings.substring(0, strings.length() - 1);
    }

    private String getValuesFormatted(Map<String, Object> input, String format) {
        StringBuilder values = new StringBuilder("");
        for (Object value : input.values()) {
            values.append(String.format(format, value));
        }
        return values.substring(0, values.length() - 1);
    }

    @Override
    public String getDatabaseName() {
        return database;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Set<String> getDatabases() {
        Set<String> list = new LinkedHashSet<>();

        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT datname FROM pg_database WHERE datistemplate = false;");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int getTableSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet tableSize = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName)) {
            tableSize.next();
            return tableSize.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage(), e.getCause());
        }
    }

    @Override // TODO заготовка, может как-нибудь реализовать в userInerface, а может и нет :) тест написан
    public void update(String tableName, int id, Map<String, Object> newValue) {
        String tableNames = getNameFormatted(newValue, "%s = ?,");
        String updateTable = "UPDATE public." + tableName + " SET " + tableNames + " WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(updateTable)) {
            int index = 1;
            for (Object value : newValue.values()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setInt(index, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteTableRow(String tableName, String id) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM PUBLIC." + tableName + " WHERE id =" + id );
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}