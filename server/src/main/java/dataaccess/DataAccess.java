package dataaccess;

import java.util.HashMap;

public class DataAccess
{
    public void getUser(String username) //this calls the db.
    {
        HashMap<String, String> temp_db = new HashMap<>();

        if (temp_db.containsKey(username)) //403 Error
        {
            //return UserData
        }
        else
        {
            temp_db.put("username", username);
        }
    }
}
