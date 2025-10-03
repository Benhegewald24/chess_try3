package dataaccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DataAccess
{
    HashSet<UserData> temp_db_user = new HashSet<>();
    HashSet<AuthData> temp_db_auth = new HashSet<>();

    public boolean getUser(String username)
    {
        // if (username in temp_db)
        //  {
        //      return UserData;
        //  }

        // else
        //  {
        //      Find UserData by username
        //  }

        //search the db for the user using the username. return the User object
    }

    public void createUser(UserData ud) throws DataAccessException
    {
        temp_db_user.add(ud);
        //add user to the database. Some object should hold all 3 pieces of info.
    }

    public void clear() //used during testing
    {
        //db.clear();
    }

    public void createGame()
    {
        //maybe this calls reset game (look at stuff from phase 1).
    }

    public void getGame()
    {
        // Retrieve a specified game with the given game ID. Not sure what form the game ID takes... string? int? JSON?
    }

    public void listGames()
    {
        //maybe returns a list of all of the game objects?
    }

    public void updateGame()
    {
        // Updates a chess game. It should replace the chess game string corresponding to a given gameID. This is used when players join a game or when a move is made.
    }

    public void createAuth(AuthData auth_data)
    {
        String authToken = auth_data.generateToken();
        temp_db_auth.add(auth_data);
        // Create a new authorization...
        // is authorizaiton the same as an authToken?
    }

    public void getAuth() //takes as input an authToken.
    {
        //Retrieves an authorization
    }

    public void deleteAuth()
    {
        //Delete an authorization so that it is no longer valid.
    }

    //the above list is not exhaustive
}
