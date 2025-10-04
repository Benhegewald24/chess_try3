package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import requests.*;
import results.*;

public class UserService
{

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException
    {
        RegisterResult reg_result = new RegisterResult();
        DataAccess da = new DataAccess();
        UserData ud = new UserData();
        AuthData ad = new AuthData();

        da.getUser(registerRequest.username);
        da.createUser(ud);
        da.createAuth(ad);

        if (da.getUser(registerRequest.username == failure))
        {
            return AlreadyTakenException;
        }

        else
        {
            return reg_result;
        }
        //the register endpoint returns an authToken in the body of responses
    }

    public LoginResult login(LoginRequest loginRequest)
    {
        LoginResult li_result = new LoginResult();
        //the login endpoint returns an authToken in the body of responses
        return li_result;
    }

    public LogoutResult logout(LogoutRequest logoutRequest)
    {
        LogoutResult lor = new LogoutResult();
        return lor;
    }

    public ListGamesRequest listgames(ListGamesRequest listRequest) //the list games endpoint provides an authToken in the HTTP authorization header.
    {
        ListGamesRequest lgr = new ListGamesRequest();
        return lgr;
    }

    public CreateGameResult creategame(CreateGameRequest createGameRequest, String authToken) //this also takes in an authToken as a second parameter!
    {
        try
        {
            CreateGameResult cgr = new CreateGameResult();
            return cgr;
        }
        catch (InvalidGameNameException igne)
        {
            throw igne;
        }
        catch (InvalidAuthTokenException iate) // Error 401 Invalid AuthToken
        {
            throw igte;
        }
    }

    public JoinGameResult joingame(JoinGameRequest joinGameRequest, String authToken) //this also takes in an authToken as a second parameter!
    {
        try
        {

            JoinGameResult jgr = new JoinGameResult();
            return jgr;
        }

        catch (InvalidGameNameException igne) // Error 400 Game does not exist
        {
            throw igne;
        }
        catch (InvalidAuthTokenException iate) // Error 401 Invalid AuthToken
        {
            throw igte;
        }
        catch (InvalidGameIDException igide) //Error 403 Game Already Full
        {
            throw igide;
        }
    }

    public static ClearResult cleargame()
    {
        try
        {
            DataAccess.clear();
            ClearResult cr = new ClearResult();
            return cr;
        }

        catch (Exception e)
        {
            throw e; //implement InternalServerError
        }
    }
}
