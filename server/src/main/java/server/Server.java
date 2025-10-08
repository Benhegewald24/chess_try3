package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.util.Map;

public class Server //The Server should be serializing and deserializing!
{

    private final Javalin server;

    public Server() // Ben's note: This is the constructor
    {
        // Register your endpoints and exception handlers here.
        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("db", this::clearDB);
        server.post("user", this::registerUser);
        server.post("session", this::logInUser);
        server.delete("session", this::logOutUser);
        server.get("game", this::listGames);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
    }

    private void joinGame(@NotNull Context context)
    {
        //this method should return {"username" : "", "authToken" : ""} which is a Json Object
    }

    private void createGame(@NotNull Context context)
    {
        //this method should return {"gameID" : 1234} which is a Json Object
    }

    private void listGames(@NotNull Context context)
    {
        //this method should return {"games" : [{"gameID": 1234, "whiteUsername" : "", "blackUsernae" : "", "gameName" : ""}]} which is a Json Object
    }

    private void logOutUser(@NotNull Context context)
    {
        //this method should return nothing?
    }

    private void logInUser(@NotNull Context context)
    {
         //this method should return {"username" : "", "authToken" : ""} which is a Json Object
    }

    private void clearDB(@NotNull Context context) //this method should return nothing?
    {
        UserService.cleargame();
    }

    private void registerUser(@NotNull Context context)
    {
        var serializer = new Gson();
        var request_object = serializer.fromJson(context.body(), Map.class);
        var response_object = serializer.toJson(request_object);
        context.result(response_object);

        //this method should return {"username" : "", "authToken" : ""} which is a Json Object
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

    public class AlreadyTakenException extends Exception
    {

    }
}
