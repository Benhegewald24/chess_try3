package model;
import java.util.HashMap;
import java.util.UUID;

public record AuthData()
{
    static String authToken;
    static String userName;
    static HashMap<String, String> my_map;

    public static String generateToken() //created when a user registers or logs in. Associates username to teh authToken
    {
        authToken = UUID.randomUUID().toString();
        my_map.put(userName, authToken);
        return authToken;
    }
}
