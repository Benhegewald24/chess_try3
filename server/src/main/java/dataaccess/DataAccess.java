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
    private final Map<String, UserData> dict_of_users = new HashMap<>();
    private final Map<Integer, GameData> dict_of_games = new HashMap<>();
    private final Map<String, AuthData> dict_of_authTokens = new HashMap<>();
    private int game_id_counter = 1;

    public void clear()
    {
        dict_of_users.clear();
        dict_of_games.clear();
        dict_of_authTokens.clear();
        game_id_counter = 1;
    }

    public void createUser(UserData user) throws DataAccessException
    {
        if (user == null || user.username() == null)
        {
            throw new DataAccessException("Invalid username");
        }

        if (dict_of_users.containsKey(user.username()))
        {
            throw new DataAccessException("Username already taken");
        }

        dict_of_users.put(user.username(), user);
    }

    public UserData getUser(String username) throws DataAccessException
    {
        if (username == null)
        {
            throw new DataAccessException("Username can't be null");
        }
        return dict_of_users.get(username);
    }

    public int createGame(String gameName) throws DataAccessException
    {
        if (gameName == null)
        {
            throw new DataAccessException("Game name can't be null / empty");
        }
        
        int gameId = game_id_counter++;
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameId, null, null, gameName, newGame);
        //Inputs for a gameData object: (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)

        dict_of_games.put(gameId, gameData);
        return gameId;
    }

    public GameData getGame(int gameID)
    {
        return dict_of_games.get(gameID);
    }

    public Collection<GameData> listGames()
    {
        return dict_of_games.values();
    }

    public void updateGame(GameData game) throws DataAccessException
    {
        if (game == null || !dict_of_games.containsKey(game.gameID()))
        {
            throw new DataAccessException("Invalid game");
        }
        dict_of_games.put(game.gameID(), game);
    }

    public void createAuth(AuthData auth) throws DataAccessException
    {
        if (auth == null || auth.authToken() == null)
        {
            throw new DataAccessException("Auth / authToken can't be null");
        }
        dict_of_authTokens.put(auth.authToken(), auth);
    }

    public AuthData getAuth(String authToken) throws DataAccessException
    {
        if (authToken == null)
        {
            throw new DataAccessException("AuthToken can't be null");
        }
        return dict_of_authTokens.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException
    {
        if (authToken == null)
        {
            throw new DataAccessException("AuthToken can't be null");
        }
        dict_of_authTokens.remove(authToken);
    }
}
