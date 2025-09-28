package server;

import io.javalin.*;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Scanner;

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
        Scanner user_input = new Scanner(System.in);
        Dictionary<String, String> dict = new Dictionary<String, String>()
        {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Enumeration<String> keys() {
                return null;
            }

            @Override
            public Enumeration<String> elements() {
                return null;
            }

            @Override
            public String get(Object key) {
                return "";
            }

            @Override
            public String put(String key, String value) {
                return "";
            }

            @Override
            public String remove(Object key) {
                return "";
            }
        };

        System.out.println("Username: ");
        String username = user_input.nextLine();
        System.out.println("Password: ");
        String password = user_input.nextLine();
        System.out.println("Email: ");
        String email = user_input.nextLine();
        // format this data into Json
        //send to Register handler (not sure where this is... reread instructions)
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
