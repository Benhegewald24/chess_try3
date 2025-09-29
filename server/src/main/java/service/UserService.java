package service;

public class UserService
{
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
}
