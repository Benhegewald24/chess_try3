package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
                        "CREATE TABLE IF NOT EXISTS user (`username` varchar(256) NOT NULL, `password` varchar(256) NOT NULL," +
                                " `email` varchar(256) NOT NULL, PRIMARY KEY (`username`))",
                        "CREATE TABLE IF NOT EXISTS game (`gameID` INT NOT NULL AUTO_INCREMENT, `whiteUsername` VARCHAR(256)," +
                                " `blackUsername` VARCHAR(256), `gameName` VARCHAR(256) NOT NULL, `gameJSON` TEXT NOT NULL, PRIMARY KEY (`gameID`))",
                        "CREATE TABLE IF NOT EXISTS auth (`authToken` varchar(256) NOT NULL, `username` varchar(256) NOT NULL," +
                                " PRIMARY KEY (`authToken`))"
                    };
            for (var statement : createStatements)
            {
                try (var preparedStatement = connection.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException(e);
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

        if (user.password() == null)
        {
            throw new DataAccessException("Error: bad request");
        }

        try
        {
            UserData existingUser = getUser(user.username());
            if (existingUser != null)
            {
                throw new DataAccessException("Username already taken");
            }
        }
        catch (SQLException ignored) {}

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
        catch (SQLException e)
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
        Gson gson = new Gson();
        String newGameJSON = gson.toJson(newGame);

        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, gameJSON) VALUES (?, ?, ?, ?)";
            try (var preparedStatement = connection.prepareStatement(statement, java.sql.Statement.RETURN_GENERATED_KEYS))
            {
                preparedStatement.setString(1, null);
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, gameName);
                preparedStatement.setString(4, newGameJSON);
                preparedStatement.executeUpdate();

                try (var generatedKeys = preparedStatement.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                    {
                        int gameID = generatedKeys.getInt(1);
                        return gameID;
                    }
                    else
                    {
                        throw new DataAccessException("createGame FAILED");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Game NOT created");
        }
    }

    public GameData getGame(int gameID)
    {
        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameJSON FROM game WHERE gameID=?";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                preparedStatement.setInt(1, gameID);
                try(var result = preparedStatement.executeQuery())
                {
                    if (result.next())
                    {
                        String gameJSON = result.getString("gameJSON");
                        Gson gson = new Gson();
                        ChessGame game = gson.fromJson(gameJSON, ChessGame.class);

                        String whiteUsername = result.getString("whiteUsername");
                        String blackUsername = result.getString("blackUsername");

                        return new GameData(result.getInt("gameID"), whiteUsername, blackUsername, result.getString("gameName"), game);
                    }
                }
            }
        }
        catch (SQLException | DataAccessException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Collection<GameData> listGames()
    {
        var games = new ArrayList<GameData>();
        try (var connection = DatabaseManager.getConnection())
        {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, gameJSON FROM game";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                try(var result = preparedStatement.executeQuery())
                {
                    Gson gson = new Gson();
                    while (result.next())
                    {
                        String gameJSON = result.getString("gameJSON");
                        ChessGame game = gson.fromJson(gameJSON, ChessGame.class);

                        String whiteUsername = result.getString("whiteUsername");
                        String blackUsername = result.getString("blackUsername");

                        GameData gameData = new GameData(result.getInt("gameID"),
                                whiteUsername, blackUsername,
                                result.getString("gameName"), game);
                        games.add(gameData);
                    }
                }
            }
        }
        catch (SQLException | DataAccessException e)
        {
            throw new RuntimeException(e);
        }
        return games;
    }

    public void updateGame(GameData game) throws DataAccessException
    {
        if (game == null)
        {
            throw new DataAccessException("Invalid game");
        }

        try (var connection = DatabaseManager.getConnection())
        {
            Gson gson = new Gson();
            String gameJSON = gson.toJson(game.game());

            var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, gameJSON=? WHERE gameID=?";
            try (var preparedStatement = connection.prepareStatement(statement))
            {
                String whiteUsername = game.whiteUsername();
                String blackUsername = game.blackUsername();

                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, gameJSON);
                preparedStatement.setInt(5, game.gameID());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            throw new DataAccessException("Game update failed");
        }
    }

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
                try (var result = preparedStatement.executeQuery())
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
            throw new DataAccessException("authToken delete unsuccessful");
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
            throw new DataAccessException("authToken delete unsuccessful");
        }
    }
}
