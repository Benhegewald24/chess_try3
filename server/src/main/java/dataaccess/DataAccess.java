package dataaccess;

import model.UserData;
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

    public void createUser(UserData ud) throws DataAccessException {}
    public void clear() {}
    public void createGame() {}
    public void getGame()  {}
    public void listGames() {}
    public void updateGame() {}
    public void createAuth() {}
    public void getAuth() {}
    public void deleteAuth() {}

}
