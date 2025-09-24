package server;

import io.javalin.*;

public class Server
{

    private final Javalin javalin;

    public Server()
    {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Ben's note: This is the constructor
        // Register your endpoints and exception handlers here.}
    }

    public void registerUser()
    {

    }

    public void logInUser()
    {

    }

    public void logOutUser()
    {

    }

    public void listGames()
    {

    }

    public void createGame()
    {

    }

    public void joinGame()
    {

    }

    public void clearDB()
    {

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
