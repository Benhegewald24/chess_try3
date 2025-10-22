package dataaccess;
import chess.ChessGame;
import model.UserData;
import model.GameData;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class DataAccess
{
    private final Map<String, UserData> dictOfUsers = new HashMap<>();
    private final Map<Integer, GameData> dictOfGames = new HashMap<>();
    private final Map<String, AuthData> dictOfAuthTokens = new HashMap<>();
    private int gameIDCounter = 1;

    public void clear()
    {
        dictOfUsers.clear();
        dictOfGames.clear();
        dictOfAuthTokens.clear();
        gameIDCounter = 1;
    }

    public void createUser(UserData user) throws DataAccessException
    {
        if (user == null || user.username() == null)
        {
            throw new DataAccessException("Invalid username");
        }

        if (dictOfUsers.containsKey(user.username()))
        {
            throw new DataAccessException("Username already taken");
        }

        dictOfUsers.put(user.username(), user);
    }

    public UserData getUser(String username) throws DataAccessException
    {
        if (username == null)
        {
            throw new DataAccessException("Username can't be null");
        }
        return dictOfUsers.get(username);
    }

    public int createGame(String gameName) throws DataAccessException
    {
        if (gameName == null)
        {
            throw new DataAccessException("Game name can't be null / empty");
        }
        
        int gameId = gameIDCounter++;
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameId, null, null, gameName, newGame);
        //Inputs for a gameData object: (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)

        dictOfGames.put(gameId, gameData);
        return gameId;
    }

    public GameData getGame(int gameID)
    {
        return dictOfGames.get(gameID);
    }

    public Collection<GameData> listGames()
    {
        return dictOfGames.values();
    }

    public void updateGame(GameData game) throws DataAccessException
    {
        if (game == null || !dictOfGames.containsKey(game.gameID()))
        {
            throw new DataAccessException("Invalid game");
        }
        dictOfGames.put(game.gameID(), game);
    }

    public void createAuth(AuthData auth) throws DataAccessException
    {
        if (auth == null || auth.authToken() == null)
        {
            throw new DataAccessException("Auth / authToken can't be null");
        }
        dictOfAuthTokens.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken) throws DataAccessException
    {
        if (authToken == null)
        {
            throw new DataAccessException("AuthToken can't be null");
        }
        return dictOfAuthTokens.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException
    {
        if (authToken == null)
        {
            throw new DataAccessException("AuthToken can't be null");
        }
        dictOfAuthTokens.remove(authToken);
    }
}
