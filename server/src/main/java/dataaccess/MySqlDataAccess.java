package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class MySqlDataAccess extends DataAccess
{
    public MySqlDataAccess() throws DataAccessException, SQLException
    {
        configureDatabase();
        createTables();
    }

    private void configureDatabase() throws DataAccessException
    {
        DatabaseManager.createDatabase();
    }

    private void createTables() throws DataAccessException, SQLException
    {
        try (var connection = DatabaseManager.getConnection())
        {
            String[] createStatements =
                    {
                        "CREATE TABLE IF NOT EXISTS user ( `username` varchar(256) NOT NULL, `password` varchar(256) NOT NULL, `email` varchar(256) NOT NULL, PRIMARY KEY (`username`))",
                        "CREATE TABLE IF NOT EXISTS game ( `gameID` INT NOT NULL AUTO_INCREMENT, `whiteUsername` VARCHAR(256), `blackUsername` VARCHAR(256), `gameName` VARCHAR(256) NOT NULL, `gameJSON` TEXT NOT NULL, PRIMARY KEY (`gameID`))",
                        "CREATE TABLE IF NOT EXISTS auth ( `username` varchar(256) NOT NULL, `authToken` varchar(256), PRIMARY KEY (`authToken`))"
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

    public void clear()
    {
        String[] statements =
                {
                "TRUNCATE user",
                "TRUNCATE game",
                "TRUNCATE auth"
        };
        try (var connection = DatabaseManager.getConnection())
        {
            for (var statement : statements)
            {
                try (var preparedStatement = connection.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException | DataAccessException e)
        {
            throw new RuntimeException(e);
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
            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());

            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
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
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, username);
                try(var result = preparedStatement.executeQuery())
                {
                    if (result.next())
                    {
                        return new UserData(result.getString("username"), result.getString("password"), result.getString("email"));
                    }
                }
            }
        }
        return null;
    }

    public int createGame(String gameName) throws DataAccessException
    {
        if (gameName == null)
        {
            throw new DataAccessException("Game name can't be null / empty");
        }

        ChessGame newGame = new ChessGame();


        GameData gameData = new GameData(gameId, null, null, gameName, newGame);
        //Inputs for a gameData object: (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)

        return gameId;
    }

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

    public void createAuth(AuthData auth) throws Exception
    {
        if (auth == null || auth.authToken() == null)
        {
            throw new DataAccessException("Auth / authToken can't be null");
        }

        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";

            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException
    {
        if (authToken == null)
        {
            throw new DataAccessException("AuthToken can't be null");
        }
        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, authToken);
                try(var result = preparedStatement.executeQuery())
                {
                    if (result.next())
                    {
                        return new AuthData(result.getString("authToken"), result.getString("username"));
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("authToken delete unsuccessful: " + e.getMessage());
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException
    {
        if (authToken == null)
        {
            throw new DataAccessException("AuthToken can't be null");
        }

        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "DELETE FROM auth WHERE authToken=?";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("authToken delete unsuccessful: " + e.getMessage());
        }
    }
}
