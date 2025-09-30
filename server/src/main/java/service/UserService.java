package service;

import dataaccess.DataAccess;
import handler.*;

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

        return reg_result;
    }

    public LoginResult login(LoginHandler loginRequest)
    {
        LoginResult li_result = new LoginResult();
        return li_result;
    }

    public void logout(LogoutHandler logoutRequest)
    {

    }

    public void listgames(ListGamesHandler listRequest)
    {

    }

    public void creategame(CreateGameHandler createGameRequest) //this also takes in an authToken as a second parameter!
    {

    }

    public void joingame(JoinGameHandler joinGameRequest) //this also takes in an authToken as a second parameter!
    {

    }

    public static void cleargame(ClearGameHandler clearGameRequest)
    {
        //if (InternalServerError)
        //{
        //  return UserData
        // }
    }
}
