package handler;

import java.util.HashMap;
import service.*;

public class RegisterHandler
{
    String username;
    String password;
    String email;

    public RegisterHandler()
    {}

    public void register_h(HashMap<String, String> register_data)   //this method should create a RegisterReguest and then send it to service
    {
        RegisterHandler registerRequest = new RegisterHandler();
        registerRequest.username = register_data.get(username);
        registerRequest.password = register_data.get(password);
        registerRequest.email = register_data.get(email);

        UserService reg = new UserService();

        reg.register(registerRequest);
    }
}
