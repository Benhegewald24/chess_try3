package handler;

import java.util.HashMap;
import service.*;

public class RegisterHandler
{
    public void register_h(HashMap<String, String> my_map)   //this method should create a RegisterReguest and then send it to service
    {
        RegisterHandler registerRequest = new RegisterHandler();
        //what should a RegisterRequest look like / contain / do?
        UserService reg = new UserService();

        reg.register(registerRequest);
    }
}
