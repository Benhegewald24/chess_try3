package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;

public class MySqlDataAccess extends DataAccess
{
    private void createTables() throws DataAccessException, SQLException
    {
        DatabaseManager.createDatabase();
        try (var connection = DatabaseManager.getConnection())
        {
            String[] createStatements =
                    {
                        "CREATE TABLE IF NOT EXISTS user ( 'username' varchar(256) NOT NULL, 'password' varchar(256) NOT NULL, 'email' varchar(256), PRIMARY KEY ('username')",
                        "CREATE TABLE IF NOT EXISTS game ( 'gameID' int NOT NULL AUTO_INCREMENT, 'whiteUsername' varchar(256), 'blackUsername' varchar(256), 'gameName' varchar(256), 'game' varchar(256), PRIMARY KEY ('gameID')",
                        "CREATE TABLE IF NOT EXISTS auth ( 'username' varchar(256) NOT NULL, 'authToken' varchar(256), PRIMARY KEY ('username')"
                    };
            for (var statement : createStatements)
            {
                try (var preparedStatement = connection.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException mse)
        {
            throw new SQLException(mse.getMessage());
        }
    }

    public void createUser(UserData user) throws Exception
    {
        if (user == null || user.username() == null)
        {
            throw new DataAccessException("Invalid username");
        }

        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.executeUpdate();
            }
//            var json = new Gson().toJson(user);
//            var id = executeUpdate(statement, user.username(), user.password(), user.email());
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
    }

    public UserData getUser(String username) throws DataAccessException, SQLException
    {
        if (username == null)
        {
            throw new DataAccessException("Username can't be null");
        }

        try (var connection = DatabaseManager.getConnection())
        {
            var sql_instructions = "SELECT username, password, email FROM user WHERE username=?";
            try (var prep = connection.prepareStatement(sql_instructions))
            {
                prep.setInt(1, username);
                try(var rs = prep.executeQuery())
                {
                    if (rs.next())
                    {
                        return idk;
                    }
                }
            }
        }
        return dictOfUsers.get(username);
    }

//    public int createGame(String gameName) throws DataAccessException
//    {
//        if (gameName == null)
//        {
//            throw new DataAccessException("Game name can't be null / empty");
//        }
//
//        int gameId = gameIDCounter++;
//        ChessGame newGame = new ChessGame();
//        GameData gameData = new GameData(gameId, null, null, gameName, newGame);
//        //Inputs for a gameData object: (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
//
//        dictOfGames.put(gameId, gameData);
//        return gameId;
//    }
//
//    public GameData getGame(int gameID)
//    {
//        return dictOfGames.get(gameID);
//    }
//
//    public Collection<GameData> listGames()
//    {
//        return dictOfGames.values();
//    }
//
//    public void updateGame(GameData game) throws DataAccessException
//    {
//        if (game == null || !dictOfGames.containsKey(game.gameID()))
//        {
//            throw new DataAccessException("Invalid game");
//        }
//        dictOfGames.put(game.gameID(), game);
//    }
//
//    public void createAuth(AuthData auth) throws DataAccessException
//    {
//        if (auth == null || auth.authToken() == null)
//        {
//            throw new DataAccessException("Auth / authToken can't be null");
//        }
//        dictOfAuthTokens.put(auth.authToken(), auth);
//    }
//
//    public AuthData getAuth(String authToken) throws DataAccessException
//    {
//        if (authToken == null)
//        {
//            throw new DataAccessException("AuthToken can't be null");
//        }
//        return dictOfAuthTokens.get(authToken);
//    }
//
//    public void deleteAuth(String authToken) throws DataAccessException
//    {
//        if (authToken == null)
//        {
//            throw new DataAccessException("AuthToken can't be null");
//        }
//        dictOfAuthTokens.remove(authToken);
//    }
}
