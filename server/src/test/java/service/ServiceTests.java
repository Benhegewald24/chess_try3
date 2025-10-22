package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;
import requests.*;
import results.*;

import static org.junit.jupiter.api.Assertions.*;

    @Test
    void registerPositiveTest() throws DataAccessException
{
    dataAccess.clear();
    userService = new UserService(dataAccess);

    RegisterRequest request = new RegisterRequest("newUser", "newPass", "new@email.com");
    RegisterResult result = userService.register(request);

    assertNotNull(result);
    assertEquals("newUser", result.username());
    assertNotNull(result.authToken());
    assertFalse(result.authToken().isEmpty());
}

@Test
void registerNegativeAlreadyTakenTest()
{
    RegisterRequest request = new RegisterRequest("JohnSmith", "otherPass", "other@email.com");
    try
    {
        userService.register(request);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("already taken"));
    }
}

@Test
void registerNegativeBadRequestTest()
{
    RegisterRequest request = new RegisterRequest(null, "pass", "email@test.com");
    try
    {
        userService.register(request);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("bad request"));
    }
}

@Test
void loginPositiveTest() throws DataAccessException
{
    LoginRequest loginRequest = new LoginRequest("JohnSmith", "password");
    LoginResult result = userService.login(loginRequest);

    assertNotNull(result);
    assertEquals("JohnSmith", result.username());
    assertNotNull(result.authToken());
    assertFalse(result.authToken().isEmpty());
}

@Test
void loginNegativeWrongPasswordTest()
{
    LoginRequest loginRequest = new LoginRequest("JohnSmith", "wrongPassword");
    try
    {
        userService.login(loginRequest);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}

@Test
void loginNegativeUserDoesNotExistTest()
{
    LoginRequest loginRequest = new LoginRequest("nonExistentUser", "somePass");
    try
    {
        userService.login(loginRequest);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}

@Test
void logoutPositiveTest() throws DataAccessException
{
    LogoutResult result = userService.logout(validAuthToken);
    assertNotNull(result);

    assertNull(dataAccess.getAuth(validAuthToken));
}

@Test
void logoutNegativeInvalidTokenTest()
{
    try
    {
        userService.logout("invalidToken123");
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}

@Test
void listGamesPositiveTest() throws DataAccessException
{
    gameService.createGame(new CreateGameRequest("Game1"), validAuthToken);
    gameService.createGame(new CreateGameRequest("Game2"), validAuthToken);

    ListGamesResult result = gameService.listGames(validAuthToken);

    assertNotNull(result);
    assertNotNull(result.games());
    assertEquals(2, result.games().size());
}

@Test
void listGamesNegativeUnauthorizedTest()
{
    try
    {
        gameService.listGames("invalidToken123");
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}

@Test
void createGamePositiveTest() throws DataAccessException
{
    CreateGameRequest request = new CreateGameRequest("MyChessGame");
    CreateGameResult result = gameService.createGame(request, validAuthToken);

    assertNotNull(result);
    assertTrue(result.gameID() > 0);

    ListGamesResult listResult = gameService.listGames(validAuthToken);
    assertEquals(1, listResult.games().size());
}

@Test
void createGameNegativeUnauthorizedTest()
{
    CreateGameRequest request = new CreateGameRequest("MyGame");
    try
    {
        gameService.createGame(request, "invalidToken123");
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}

@Test
void createGameNegativeBadRequestTest()
{
    CreateGameRequest request = new CreateGameRequest("");
    try
    {
        gameService.createGame(request, validAuthToken);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("bad request"));
    }
}

@Test
void joinGamePositiveTest() throws DataAccessException
{
    CreateGameResult createResult = gameService.createGame(
            new CreateGameRequest("TestGame"), validAuthToken);
    int gameId = createResult.gameID();

    JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameId);
    JoinGameResult result = gameService.joinGame(joinRequest, validAuthToken);

    assertNotNull(result);
}

@Test
void joinGameNegativeUnauthorizedTest() throws DataAccessException
{
    CreateGameResult createResult = gameService.createGame(
            new CreateGameRequest("TestGame"), validAuthToken);

    JoinGameRequest joinRequest = new JoinGameRequest("WHITE", createResult.gameID());
    try
    {
        gameService.joinGame(joinRequest, "invalidToken");
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("unauthorized"));
    }
}

@Test
void joinGameNegativeAlreadyTakenTest() throws DataAccessException
{
    CreateGameResult createResult = gameService.createGame(
            new CreateGameRequest("TestGame"), validAuthToken);
    int gameId = createResult.gameID();

    JoinGameRequest joinRequest1 = new JoinGameRequest("WHITE", gameId);
    gameService.joinGame(joinRequest1, validAuthToken);

    RegisterRequest registerRequest = new RegisterRequest("user2", "password2", "user2@test.com");
    RegisterResult registerResult = userService.register(registerRequest);
    String authToken2 = registerResult.authToken();

    JoinGameRequest joinRequest2 = new JoinGameRequest("WHITE", gameId);
    try
    {
        gameService.joinGame(joinRequest2, authToken2);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("already taken"));
    }
}

@Test
void joinGameNegativeInvalidGameIDTest()
{
    JoinGameRequest joinRequest = new JoinGameRequest("WHITE", 99999);
    try
    {
        gameService.joinGame(joinRequest, validAuthToken);
        fail("Expected DataAccessException to be thrown");
    }
    catch (DataAccessException exception)
    {
        assertTrue(exception.getMessage().contains("bad request"));
    }
}
}

