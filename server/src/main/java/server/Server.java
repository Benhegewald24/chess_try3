package server;

import handler.*;
import io.javalin.*;
import io.javalin.http.Handler;

import java.util.*;

public class Server //The Server should be serializing and deserializing!
{

    private final Javalin javalin;

    public Server()
    {
        // Ben's note: This is the constructor
        // Register your endpoints and exception handlers here.}
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", clearDB());
        javalin.post("/user", registerUser());
        javalin.post("/session", logInUser());
        javalin.delete("/session", logOutUser());
        javalin.get("/game", listGames());
        javalin.post("game", createGame());
        javalin.put("/game", joinGame());
    }

    public Handler registerUser()
    {
        RegisterHandler rr = new RegisterHandler();
        Scanner user_input = new Scanner(System.in);
        HashMap<String, String> my_map = new HashMap<>();

        System.out.println("Username: ");
        String username = user_input.nextLine();
        System.out.println("Password: ");
        String password = user_input.nextLine();
        System.out.println("Email: ");
        String email = user_input.nextLine();
        my_map.put("username",username);
        my_map.put("password", password);
        my_map.put("email", email);

        rr.register_h(my_map); //send map to Register handler (RegisterHandler)
        return null;
    }

    public Handler logInUser()
    {
        // print to screen: "Username: "
        // store user input
        // print to screen: "Password: "
        // password (initialized above) = userinput (whatever the user types)
        return null;
    }

    public Handler logOutUser()
    {

        return null;
    }

    public Handler listGames()
    {

        return null;
    }

    public Handler createGame()
    {

        return null;
    }

    public Handler joinGame()
    {

        return null;
    }

    public Handler clearDB()
    {
        ClearGameHandler clear_game_request = new ClearGameHandler();
        clear_game_request.clear(clear_game_request);
        return null;
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
