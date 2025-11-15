package client;
import org.junit.jupiter.api.*;
import server.Server;
import static chess.ChessGame.TeamColor.*;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests
{
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init()
    {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    void clearDatabase() throws Exception
    {
        facade.clear();
    }

    @AfterAll
    static void stopServer()
    {
        server.stop();
    }

    @Test
    void registerSuccess() throws Exception
    {
        var authData = facade.register("Ben", "password", "Ben@gmail.com");
        assertTrue(authData.authToken().length() > 8);
    }

    @Test
    void registerFailUserAlreadyTaken() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        Exception ex = assertThrows(Exception.class, () -> facade.register("Ben", "password", "Ben@gmail.com"));
        assertTrue(ex.getMessage().toLowerCase().contains("already"));
    }

    @Test
    void loginSuccess() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        var authData = facade.login("Ben", "password");
        assertEquals("Ben", authData.username());
    }

    @Test
    void loginFailIncorrectPassword() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        Exception ex = assertThrows(Exception.class, () -> facade.login("Ben", "notThePassword"));
        assertTrue(ex.getMessage().toLowerCase().contains("unauthorized"));
    }

    @Test
    void logoutSuccess() throws Exception
    {
        var auth = facade.register("Ben", "password", "Ben@gmail.com");
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    void logoutFailsBadToken() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        assertThrows(Exception.class, () -> facade.logout("bad-token"));
    }

    @Test
    void logoutRevokesAccess() throws Exception
    {
        var auth = facade.register("Ben", "password", "Ben@gmail.com");
        facade.logout(auth.authToken());
        assertThrows(Exception.class, () -> facade.listGames(auth.authToken()));
    }

    @Test
    void listGamesEmpty() throws Exception
    {
        var auth = facade.register("Ben", "password", "Ben@gmail.com");
        var result = facade.listGames(auth.authToken());
        assertTrue(result.games().isEmpty());
    }

    @Test
    void listGamesFailsBadToken() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        assertThrows(Exception.class, () -> facade.listGames("bad-token"));
    }

    @Test
    void createGameAppearsInList() throws Exception
    {
        var auth = facade.register("Ben", "password", "Ben@gmail.com");
        var created = facade.createGame(auth.authToken(), "My Game");
        var result = facade.listGames(auth.authToken());

        assertEquals(1, result.games().size());
        assertEquals(created.gameID(), result.games().iterator().next().gameID());
    }

    @Test
    void createGameFailsBadToken() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        assertThrows(Exception.class, () -> facade.createGame("bad-token", "game1"));
    }

    @Test
    void joinGameSuccess() throws Exception
    {
        var user1 = facade.register("Ben", "password", "Ben@gmail.com");
        var user2 = facade.register("Ben2", "password2", "Ben2@gmail.com");
        var created = facade.createGame(user1.authToken(), "gameName");

        facade.joinGame(user1.authToken(), created.gameID(), WHITE);
        assertDoesNotThrow(() -> facade.joinGame(user2.authToken(), created.gameID(), BLACK));
    }

    @Test
    void joinGameFailsColorAlreadyTaken() throws Exception
    {
        var user1 = facade.register("Ben", "password", "Ben@gmail.com");
        var user2 = facade.register("Ben2", "password2", "Ben2@gmail.com");
        var created = facade.createGame(user1.authToken(), "gameName");

        facade.joinGame(user1.authToken(), created.gameID(), WHITE);
        assertThrows(Exception.class, () -> facade.joinGame(user2.authToken(), created.gameID(), WHITE));
    }

    @Test
    void observeGameSuccess() throws Exception
    {
        var user1 = facade.register("Ben", "password", "Ben@gmail.com");
        var user2 = facade.register("Ben2", "password2", "Ben2@gmail.com");
        var created = facade.createGame(user1.authToken(), "gameToWatch");

        facade.observeGame(user2.authToken(), created.gameID());
        var games = facade.listGames(user2.authToken());
        assertEquals(1, games.games().size());
    }

    @Test
    void observeGameFailsBadToken() throws Exception
    {
        var user1 = facade.register("Ben", "password", "Ben@gmail.com");
        var created = facade.createGame(user1.authToken(), "Observation Game");
        assertThrows(Exception.class, () -> facade.observeGame("bad-token", created.gameID()));
    }
}
