package server;
import service.UserService;
import service.GameService;
import dataaccess.MySqlDataAccess;
import results.*;
import requests.*;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import java.sql.SQLException;
import java.util.Map;
import com.google.gson.Gson;

public class Server
{
    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;
    private final Gson gson;

    public Server()
    {
        MySqlDataAccess dataAccess;
        try
        {
            dataAccess = new MySqlDataAccess();
        }
        catch (DataAccessException | SQLException e)
        {
            throw new RuntimeException("Failed to initialize database");
        }

        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);
        gson = new Gson();

        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("/db", this::clearHandler);
        server.post("/user", this::registerHandler);
        server.post("/session", this::loginHandler);
        server.delete("/session", this::logoutHandler);
        server.get("/game", this::listGamesHandler);
        server.post("/game", this::createGameHandler);
        server.put("/game", this::joinGameHandler);
        server.exception(Exception.class, this::exceptionHandler);
    }

    public void clearHandler(Context context)
    {
        try
        {
            userService.clear();
            context.status(200);
            context.result("{}");
        }
        catch (RuntimeException e)
        {
            context.status(500);
            context.result(gson.toJson(Map.of("message", "Error")));
        }
    }

    public void registerHandler(Context context)
    {
        try
        {
            RegisterRequest request = gson.fromJson(context.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            context.status(200);
            context.result(gson.toJson(result));
        }

        catch (DataAccessException e)
        {
            handleDataAccessException(context, e);
        }
        catch (Exception e)
        {
            context.status(500);
            context.result(gson.toJson(Map.of("message", "Error")));
        }
    }

    public void loginHandler(Context context)
    {
        try
        {
            LoginRequest request = gson.fromJson(context.body(), LoginRequest.class);
            LoginResult result = userService.login(request);
            context.status(200);
            context.result(gson.toJson(result));
        }

        catch (DataAccessException e)
        {
            handleDataAccessException(context, e);
        }
        catch (Exception e)
        {
            context.status(500);
            context.result(gson.toJson(Map.of("message", "Error")));
        }
    }

    public void logoutHandler(Context context)
    {
        try
        {
            String authToken = context.header("authorization");

            if (authToken == null)
            {
                context.status(401);
                context.result(gson.toJson(Map.of("message", "Error: unauthorized")));
                return;
            }

            userService.logout(authToken);
            context.status(200);
            context.result("{}");
        }
        catch (DataAccessException e)
        {
            handleDataAccessException(context, e);
        }
        catch (Exception e)
        {
            context.status(500);
            context.result(gson.toJson(Map.of("message", "Error")));
        }
    }

    private void listGamesHandler(Context context)
    {
        try
        {
            String authToken = context.header("authorization");
            if (authToken == null)
            {
                context.status(401);
                context.result(gson.toJson(Map.of("message", "Error: unauthorized")));
                return;
            }

            ListGamesResult result = gameService.listGames(authToken);
            context.status(200);
            context.result(gson.toJson(result));
        }

        catch (DataAccessException dae)
        {
            handleDataAccessException(context, dae);
        }
        catch (Exception e)
        {
            context.status(500);
            context.result(gson.toJson(Map.of("message", "Error")));
        }
    }

    private void createGameHandler(Context context)
    {
        try
        {
            String authToken = context.header("authorization");
            if (authToken == null)
            {
                context.status(401);
                context.result(gson.toJson(Map.of("message", "Error: unauthorized")));
                return;
            }

            CreateGameRequest request = gson.fromJson(context.body(), CreateGameRequest.class);
            CreateGameResult result = gameService.createGame(request, authToken);
            context.status(200);
            context.result(gson.toJson(result));
        }

        catch (DataAccessException e)
        {
            handleDataAccessException(context, e);
        }
    }

    private void joinGameHandler(Context context)
    {
        try
        {
            String authToken = context.header("authorization");
            if (authToken == null)
            {
                context.status(401);
                context.result(gson.toJson(Map.of("message", "Error: unauthorized")));
                return;
            }

            JoinGameRequest request = gson.fromJson(context.body(), JoinGameRequest.class);
            gameService.joinGame(request, authToken);
            context.status(200);
            context.result("{}");
        }

        catch (DataAccessException e)
        {
            handleDataAccessException(context, e);
        }
        catch (Exception e)
        {
            context.status(500);
            context.result(gson.toJson(Map.of("message", "Error")));
        }
    }

    private void handleDataAccessException(Context context, DataAccessException e)
    {
        String message = e.getMessage();

        if (message.contains("bad request"))
        {
            context.status(400);
        }

        else if (message.contains("unauthorized"))
        {
            context.status(401);
        }

        else if (message.contains("already taken") || message.contains("already created"))
        {
            context.status(403);
        }

        else
        {
            context.status(500);
        }
        context.result(gson.toJson(Map.of("message", message)));
    }

    private void exceptionHandler(Exception exception, Context context)
    {
        context.status(500);
        context.result(gson.toJson(Map.of("message", "Error")));
    }

    public int run(int desiredPort)
    {
        server.start(desiredPort);
        return server.port();
    }

    public void stop()
    {
        server.stop();
    }
}

