package server;
import service.UserService;
import service.GameService;
import dataaccess.DataAccess;
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
        var dataAccess = new DataAccess();
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

    private void clearHandler(Context context)
    {
        userService.clear();
        context.status(200);
        context.result("{}");
    }
    
    private void registerHandler(Context context) 
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
            throw new RuntimeException(e);
        }
    }
   
    private void loginHandler(Context context) 
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
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
  
    private void logoutHandler(Context context) 
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
            
            context.status(200);
            context.result("{}");
        }

        catch (Exception e)
        {
            throw new RuntimeException(e);
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
        
        else if (message.contains("already taken")) 
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
        context.result(gson.toJson(Map.of("message", "Error: " + exception.getMessage())));
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

