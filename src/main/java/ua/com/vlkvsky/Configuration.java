package ua.com.vlkvsky;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static final String CONFIGURATION_PROPERTIES = "configuration/sqlcmd.properties";
    private Properties properties;

    public Configuration() {
        FileInputStream fileInput = null;
        properties = new Properties();
        File file = new File(CONFIGURATION_PROPERTIES);
        try {
            fileInput = new FileInputStream(file);
            properties.load(fileInput);
        } catch (Exception e) {
            System.out.println("Error loading config " + file.getAbsolutePath());
            e.printStackTrace();
        } finally {
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {
                    // do nothing;
                }
            }
        }
    }

    public String getDbHost() {
        return properties.getProperty("db.host");
    }
    public String getDbPort() {
        return properties.getProperty("db.port");
    }
    public String getDbDriver() {
        return properties.getProperty("jdbc.driver");
    }
    public String getDbName() {
        return properties.getProperty("db.name");
    }
    public String getUsername() {
        return properties.getProperty("db.username");
    }
    public String getTestDb() {
        return properties.getProperty("db.test");
    }
    public String getPassword() {
        return properties.getProperty("db.password");
    }
}
