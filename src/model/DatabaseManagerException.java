package src.model;

import java.sql.SQLException;

public class DatabaseManagerException extends RuntimeException {

    public DatabaseManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
