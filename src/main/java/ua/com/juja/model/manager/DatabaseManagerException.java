package ua.com.juja.model.manager;

public class DatabaseManagerException extends RuntimeException {

    public DatabaseManagerException(String message, Throwable cause){
        super(message, cause);
    }
}
