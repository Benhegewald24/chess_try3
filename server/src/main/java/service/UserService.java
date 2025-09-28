package service;

public class UserService
{
    public RegisterResult register(RegisterRequest registerRequest)
    {
        RegisterResult reg_result = new RegisterResult();
        return reg_result;
    }

    public LoginResult login(LoginRequest loginRequest)
    {
        LoginResult li_result = new LoginResult();
        return li_result;
    }

    public void logout(LogoutRequest logoutRequest)
    {
    }
}
