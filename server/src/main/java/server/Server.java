package server;

import handler.*;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.ClearRequest;

import java.util.*;

public class Server //The Server should be serializing and deserializing!
{

    private final Javalin javalin;

    public Server()
    {   // Ben's note: This is the constructor
        // Register your endpoints and exception handlers here.
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", this::clearDB);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::logInUser);
        javalin.delete("/session", this::logOutUser);
        javalin.get("/game", this::listGames);
        javalin.post("game", this::createGame);
        javalin.put("/game", this::joinGame);
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
        // print to screen: "Username: "
        // store user input
        // print to screen: "Password: "
        // password (initialized above) = userinput (whatever the user types)

         //this method should return {"username" : "", "authToken" : ""} which is a Json Object
    }

    private void clearDB(@NotNull Context context)
    {
        ClearRequest clearRequest = new ClearRequest();
        clearDB(clearRequest);
        //this method should return nothing?
    }

    private void registerUser(@NotNull Context context)
    {
        context.result();

        //this method should return {"username" : "", "authToken" : ""} which is a Json Object
    }

    public int run(int desiredPort)
    {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop()
    {
        javalin.stop();

    }
}
