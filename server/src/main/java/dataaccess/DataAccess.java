package dataaccess;

import model.UserData;
import java.util.HashMap;

public class DataAccess
{
    public void getUser(String username)
    {
        //search the db for the user using the username. return the User object
    }

    public void createUser(UserData ud) throws DataAccessException
    {
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

    public void createAuth()
    {
        // Create a new authorization... I thought this was already done in AuthData Class... so maybe just call that class for an Authtoken?
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
