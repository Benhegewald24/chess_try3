package server;

import handler.*;
import io.javalin.*;

import java.util.*;

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
    }

    public void logInUser()
    {
        // print to screen: "Username: "
        // store user input
        // print to screen: "Password: "
        // password (initialized above) = userinput (whatever the user types)
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
