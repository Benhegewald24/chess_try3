package service;

import dataaccess.DataAccess;
import handler.*;
import model.UserData;

public class UserService
{

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterHandler registerRequest)
    {
        RegisterResult reg_result = new RegisterResult();
        DataAccess da = new DataAccess();
        UserData ud = new UserData();

        da.getUser(registerRequest.username);
        da.createUser(Userdata);
        da.createAuth();

        return reg_result;

        //the register endpoint returns an authToken in the body of responses
    }

    public LoginResult login(LoginHandler loginRequest)
    {
        LoginResult li_result = new LoginResult();
        //the login endpoint returns an authToken in the body of responses
        return li_result;
    }

    public void logout(LogoutHandler logoutRequest)
    {

    }

    public void listgames(ListGamesHandler listRequest)
    {
        //the list games endpoint provides an authToken in the HTTP authorization header.
    }

    public void creategame(CreateGameHandler createGameRequest) //this also takes in an authToken as a second parameter!
    {

    }

    public JoinGameResponse joingame(JoinGameHandler joinGameRequest) //this also takes in an authToken as a second parameter!
    {
        JoinGameResponse jg_response= new JoinGameResponse();
        return jg_response;
    }

    public static void cleargame(ClearGameHandler clearGameRequest)
    {
        //if (InternalServerError)
        //{
        //  return UserData
        // }
    }
}
