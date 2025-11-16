package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlDataAccessTests
{
    private MySqlDataAccess dataAccess;

    @BeforeEach
    void setUp() throws Exception
    {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
    }

    @Test
    void clearPositiveTest() throws Exception
    {
        dataAccess.createUser(new UserData("user1", "hashed1", "email1@test.com"));
        dataAccess.createAuth(new AuthData("token1", "user1"));
        dataAccess.createGame("Game1");
        dataAccess.clear();
        assertNull(dataAccess.getUser("user1"));
        assertNull(dataAccess.getAuth("token1"));
        assertTrue(dataAccess.listGames().isEmpty());
    }

    @Test
    void createUserPositiveTest() throws Exception
    {
        dataAccess.createUser(new UserData("Ben", "password123", "ben@byu.edu"));
        assertEquals("Ben", dataAccess.getUser("Ben").username());
        assertEquals("password123", dataAccess.getUser("Ben").password());
        assertEquals("ben@byu.edu", dataAccess.getUser("Ben").email());
    }

    @Test
    void createUserNegativeTest()
    {
        UserData user = new UserData(null, "password123", "ben@byu.edu");
        try
        {
            dataAccess.createUser(user);
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage().contains("Invalid username"));
        }
    }

    @Test
    void getUserPositiveTest() throws Exception
    {
        UserData user = new UserData("Ben", "password123", "ben@byu.edu");
        dataAccess.createUser(new UserData("Ben", "password123", "ben@byu.edu"));
        assertEquals(user, dataAccess.getUser("Ben"));
    }

    @Test
    void getUserNegativeTest()
    {
        UserData user = new UserData(null, "password123", "ben@byu.edu");

        try { dataAccess.getUser(user.username()); }
        catch (Exception e) { assertTrue(e.getMessage().contains("Username can't be null")); }
    }

    @Test
    void createGamePositiveTest() throws Exception
    {
        GameData game = new GameData(1, null, null, "game1", new ChessGame());
        dataAccess.createGame("game1");
        assertEquals(2, dataAccess.createGame("game1"));
    }

    @Test
    void createGameNegativeTest()
    {
        try { dataAccess.createGame(null); }
        catch (Exception e) { assertTrue(e.getMessage().contains("Game name can't be null / empty"));}
    }


    @Test
    void getGamePositiveTest() throws Exception
    {
        GameData game = new GameData(1, null, null, "game1", new ChessGame());
        dataAccess.createGame(game.gameName());
        assertEquals(game, dataAccess.getGame(1));
    }

    @Test
    void getGameNegativeTest() throws Exception
    {
        assertNull(dataAccess.getGame(999));
    }

    @Test
    void listGamePositiveTest() throws Exception
    {
        GameData game1 = new GameData(1, null, null, "game1", new ChessGame());
        GameData game2 = new GameData(2, null, null, "game2", new ChessGame());
        GameData game3 = new GameData(3, null, null, "game3", new ChessGame());
        dataAccess.createGame(game1.gameName());
        dataAccess.createGame(game2.gameName());
        dataAccess.createGame(game3.gameName());

        var games = new ArrayList<GameData>();
        games.add(game1);
        games.add(game2);
        games.add(game3);

        assertEquals(games, dataAccess.listGames());
    }

    @Test
    void updateGamePositiveTest() throws Exception
    {
        GameData game1 = new GameData(1, "white", "black", "game1", new ChessGame());
        GameData game2 = new GameData(1, "w", "b", "game2", new ChessGame());

        dataAccess.createGame(game1.gameName());
        dataAccess.updateGame(game2);
        assertEquals(game2, dataAccess.getGame(game2.gameID()));
    }

    @Test
    void updateGameNegativeTest()
    {
        GameData game = new GameData(1, null, null, "gamefortest", null);

        try { dataAccess.updateGame(game);}
        catch (Exception e) { assertTrue(e.getMessage().contains("Invalid game.")); }
    }

    @Test
    void createAuthPositiveTest() throws Exception
    {
        AuthData auth = new AuthData("secretAuth123", "username");

        dataAccess.createAuth(auth);
        assertEquals(auth, dataAccess.getAuth(auth.authToken()));
    }

    @Test
    void createAuthNegativeTest()
    {
        AuthData auth = new AuthData(null, "username");

        try { dataAccess.createAuth(auth);}
        catch (Exception e) { assertTrue(e.getMessage().contains("Auth / authToken can't be null.")); }
    }

    @Test
    void getAuthPositiveTest() throws Exception
    {
        AuthData auth = new AuthData("secretAuth123", "username");

        dataAccess.createAuth(auth);
        assertEquals(auth, dataAccess.getAuth(auth.authToken()));
    }

    @Test
    void getAuthNegativeTest()
    {
        AuthData auth = new AuthData(null, "username");

        try { dataAccess.getAuth(auth.authToken());}
        catch (Exception e) { assertTrue(e.getMessage().contains("AuthToken can't be null.")); }
    }

    @Test
    void deleteAuthPositiveTest() throws Exception
    {
        AuthData auth = new AuthData("secretAuth123", "username");

        dataAccess.createAuth(auth);
        dataAccess.deleteAuth(auth.authToken());
        assertNull(dataAccess.getAuth(auth.authToken()));
    }

    @Test
    void deleteAuthNegativeTest()
    {
        AuthData auth = new AuthData(null, "username");

        try { dataAccess.deleteAuth(auth.authToken());}
        catch (Exception e) { assertTrue(e.getMessage().contains("AuthToken can't be null.")); }
    }
}
