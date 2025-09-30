package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
//This exception should be thrown by data access methods that could fail. If a method call fails, it should throw a DataAccessException. For example, the DataAccessException is thrown if a user attempts to update a non-existent game
