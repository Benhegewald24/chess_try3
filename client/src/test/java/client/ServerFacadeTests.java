package client;
import org.junit.jupiter.api.*;
import server.Server;
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
    void registerDuplicateFails() throws Exception
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
    void loginBadPasswordFails() throws Exception
    {
        facade.register("Ben", "password", "Ben@gmail.com");
        Exception ex = assertThrows(Exception.class, () -> facade.login("Ben", "wrong"));
        assertTrue(ex.getMessage().toLowerCase().contains("unauthorized"));
    }

    @Test
    void listGamesEmpty() throws Exception
    {
        var auth = facade.register("Ben", "password", "Ben@gmail.com");
        var result = facade.listGames(auth.authToken());
        assertTrue(result.games().isEmpty());
    }
}
