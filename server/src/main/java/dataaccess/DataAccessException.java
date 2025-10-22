package dataaccess;

public class DataAccessException extends Exception
{
    public DataAccessException(String message)
    {
        super(message);
    }
}
//This exception should be thrown by data access methods that could fail. If a method call fails,
//it should throw a DataAccessException. For example, the DataAccessException is thrown if a user 
//attempts to update a non-existent game

