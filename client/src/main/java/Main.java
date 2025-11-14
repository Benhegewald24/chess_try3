import java.util.Scanner;
import chess.ChessGame;
import results.LoginResult;

public class Main
{
    private static Scanner SCANNER = new Scanner(System.in);
    private static String SERVER_URL = "http://localhost:8080";
    private static ServerFacade SERVER_FACADE = new ServerFacade(SERVER_URL);
    record loggedIn(boolean state) {}
    record loggedOut(boolean state) {}
    record inGame(boolean state) {}
    enum State {loggedIn, loggedOut, inGame}
    static State currentState = State.loggedOut;

    public static void main(String[] args)
    {
        System.out.println("Welcome to 240 chess. Type [H]elp to get started.");

        while (currentState == State.loggedOut)
        {
            String userInput = SCANNER.nextLine().trim();

            if (userInput.equalsIgnoreCase("help") || userInput.equalsIgnoreCase("h"))
            {
                help();
            }

            else if (userInput.equalsIgnoreCase("log in") || userInput.equalsIgnoreCase("l"))
            {
                login();
            }

            else if (userInput.equalsIgnoreCase("register") || userInput.equalsIgnoreCase("r"))
            {
                register();
            }

            else if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("q"))
            {
                System.out.println("Thanks for playing!");
                System.exit(0);
            }

            else
            {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void help()
    {
        System.out.println("What would you like to do?");
        System.out.println("1. [L]og In");
        System.out.println("2. [R]egister");
        System.out.println("3. [Q]uit");
    }

    private static void login()
    {
        System.out.print("Username: ");
        var username = SCANNER.nextLine().trim();

        System.out.print("Password: ");
        var password = SCANNER.nextLine();

        try
        {
            LoginResult activeSession = SERVER_FACADE.login(username, password);
            currentState = State.loggedIn;
            System.out.println("Welcome back, " + username + "!");
        }
        catch (Exception ex)
        {
            System.out.println("Login failed");
        }
    }

    private static void register()
    {
        System.out.print("Username: ");
        var username = SCANNER.nextLine().trim();

        System.out.print("Password: ");
        var password = SCANNER.nextLine();

        System.out.print("Email: ");
        var email = SCANNER.nextLine();

        try
        {
            //activeSession = SERVER_FACADE.register(username, password, email);
            currentState = State.loggedIn;
            System.out.println("Welcome to chess 240, " + username + "!");
        }
        catch (Exception ex)
        {
            System.out.println("Registrationfailed");
        }
    }

      private static void loggedInUI()
    {
        while (currentState == State.loggedIn)
        {
            System.out.println("You are now logged in.\nType [H]elp for options");
            String userInput = SCANNER.nextLine().trim();

            if (userInput.equalsIgnoreCase("help") || userInput.equalsIgnoreCase("h"))
            {
                help_logged_in();
            }
        }
    }

    public static void help_logged_in()
    {
        System.out.println("What would you like to do?");
        System.out.println("1. [L]og Out");
        System.out.println("2. [C]reate Game");
        System.out.println("3. [Li]st Games");
        System.out.println("4. [P]lay Game");
        System.out.println("5. [O]bserve Game");
        String userInput = SCANNER.nextLine().trim();

        if (userInput.equalsIgnoreCase("log out") || userInput.equalsIgnoreCase("l"))
        {
            //Calls the server logout API to logout user
            //logout request?
            currentState = State.loggedOut;
            //call main to go back to the beginning
        }

        else if (userInput.equalsIgnoreCase("create game") || userInput.equalsIgnoreCase("c"))
        {
            System.out.println("New game name: ");
            String new_game_name = SCANNER.nextLine().trim();
            //create game request?
        }

        else if (userInput.equalsIgnoreCase("list games") || userInput.equalsIgnoreCase("li"))
        {
            //list games()
        }

        else if (userInput.equalsIgnoreCase("play game") || userInput.equalsIgnoreCase("p"))
        {
            System.out.println("Which game would you like to join?: ");
            String game_id = SCANNER.nextLine().trim();
            System.out.println("Which color would you like to be? [W]hite or [B]lack?");
            String color = SCANNER.nextLine().trim();
            if (color.equalsIgnoreCase("w")) { color = String.valueOf(ChessGame.TeamColor.WHITE); }
            else {color = String.valueOf(ChessGame.TeamColor.BLACK);}

            //join game()
        }

        else if (userInput.equalsIgnoreCase("observe game") || userInput.equalsIgnoreCase("o"))
        {
            System.out.println("Which game would you like to observe?: ");
            String game_id = SCANNER.nextLine().trim();
            //observe games()
        }

        else
        {
            System.out.println("Invalid input.");
            help_logged_in();
        }
    }
}