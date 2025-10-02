package model;
import java.util.HashMap;
import java.util.UUID;

public record AuthData() //Authdata couples authToken to username... so from AuthData you can access UserData
{
    static String authToken;
    static String userName;
    static HashMap<String, String> my_map;

    public static String generateToken() //created when a user registers or logs in. Associates username to the authToken
    {
        authToken = UUID.randomUUID().toString();
        my_map.put(userName, authToken);
        return authToken;
    }
}
