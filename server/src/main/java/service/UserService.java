package service;
import results.*;
import requests.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;
import org.mindrot.jbcrypt.*;


public class UserService
{
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest request) throws Exception
    {
        if (request.username() == null || request.password() == null)
        {
            throw new DataAccessException("Error: bad request");
        }

        UserData existingUser = dataAccess.getUser(request.username());

        if (existingUser != null)
        {
            throw new DataAccessException("Error: already taken");
        }

        UserData newUser = new UserData(request.username(), request.password(), request.email());
        dataAccess.createUser(newUser);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, request.username());
        dataAccess.createAuth(authData);

        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws Exception {
        if (request.username() == null || request.password() == null)
        {
            throw new DataAccessException("Error: bad request");
        }

        UserData user = dataAccess.getUser(request.username());
        if (user == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }

        if (!BCrypt.checkpw(request.password(), user.password()))
        {
            throw new DataAccessException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, request.username());
        dataAccess.createAuth(authData);

        return new LoginResult(user.username(), authToken);
    }

    public LogoutResult logout(String authToken) throws DataAccessException
    {
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null)
        {
            throw new DataAccessException("Error: unauthorized");
        }

        dataAccess.deleteAuth(authToken);
        return new LogoutResult();
    }

    public ClearResult clear()
    {
        dataAccess.clear();
        return new ClearResult();
    }
}
