package client;
import chess.ChessGame;
import com.google.gson.Gson;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.CreateGameResult;
import results.ListGamesResult;
import results.LoginResult;
import results.RegisterResult;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade
{
    private final Gson gson = new Gson();
    private final String baseUrl;
    private final HttpClient http = HttpClient.newHttpClient();

    public ServerFacade(String baseUrl)
    {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public ServerFacade(int port)
    {
        this("http://localhost:" + port);
    }

    public LoginResult login(String username, String password) throws Exception
    {
        var requestBody = gson.toJson(new LoginRequest(username, password));
        var request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/session")).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        var response = send(request);
        ensureSuccess(response);
        return gson.fromJson(response.body(), LoginResult.class);
    }

    public RegisterResult register(String username, String password, String email) throws Exception
    {
        var body = gson.toJson(new RegisterRequest(username, password, email));
        var request = jsonRequest("/user").POST(HttpRequest.BodyPublishers.ofString(body)).build();
        var response = send(request);
        ensureSuccess(response);
        return gson.fromJson(response.body(), RegisterResult.class);
    }

    public void logout(String authToken) throws Exception
    {
        var request = authorizedRequest("/session", authToken).DELETE().build();
        var response = send(request);
        ensureSuccess(response);
    }

    public ListGamesResult listGames(String authToken) throws Exception
    {
        var request = authorizedRequest("/game", authToken).GET().build();
        var response = send(request);
        ensureSuccess(response);
        return gson.fromJson(response.body(), ListGamesResult.class);
    }

    public CreateGameResult createGame(String authToken, String gameName) throws Exception
    {
        var body = gson.toJson(new CreateGameRequest(gameName));
        var request = authorizedJsonRequest("/game", authToken).POST(HttpRequest.BodyPublishers.ofString(body)).build();
        var response = send(request);
        ensureSuccess(response);
        return gson.fromJson(response.body(), CreateGameResult.class);
    }

    public void observeGame(String authToken, int gameId) throws Exception
    {
        var body = gson.toJson(new JoinGameRequest(null, gameId));
        sendJoin(authToken, body);
    }

    public void joinGame(String authToken, int gameId, ChessGame.TeamColor color) throws Exception
    {
        var body = gson.toJson(new JoinGameRequest(color.name(), gameId));
        sendJoin(authToken, body);
    }

    public void clear() throws Exception
    {
        var request = jsonRequest("/db").DELETE().build();
        var response = send(request);
        ensureSuccess(response);
    }

    private void sendJoin(String authToken, String body) throws Exception
    {
        var request = authorizedJsonRequest("/game", authToken).PUT(HttpRequest.BodyPublishers.ofString(body)).build();
        var response = send(request);
        ensureSuccess(response);
    }

    private HttpRequest.Builder authorizedRequest(String path, String authToken)
    {
        return HttpRequest.newBuilder().uri(URI.create(baseUrl + path)).header("authorization", authToken);
    }

    private HttpRequest.Builder jsonRequest(String path)
    {
        return HttpRequest.newBuilder().uri(URI.create(baseUrl + path)).header("Content-Type", "application/json");
    }

    private HttpRequest.Builder authorizedJsonRequest(String path, String authToken)
    {
        return authorizedRequest(path, authToken).header("Content-Type", "application/json");
    }

    private HttpResponse<String> send(HttpRequest request) throws Exception
    {
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void ensureSuccess(HttpResponse<String> response) throws Exception
    {
        if (response.statusCode() >= 200 && response.statusCode() < 300)
        {
            return;
        }
        throw new Exception(extractMessage(response.body()));
    }

    private record ErrorResponse(String message) {}

    private String extractMessage(String body)
    {
        try
        {
            var error = gson.fromJson(body, ErrorResponse.class);
            if (error != null && error.message != null && !error.message.isBlank())
            {
                return error.message;
            }
        }
        catch (Exception ignored) {}
        return "Unexpected error from server";
    }
}
