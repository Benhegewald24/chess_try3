import java.util.Scanner;
import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;
import results.LoginResult;
import results.RegisterResult;
import ui.DrawBoard;
import static chess.ChessGame.TeamColor.*;

public class Main
{
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String SERVER_URL = "http://localhost:8080";
    private static final ServerFacade SERVER_FACADE = new ServerFacade(SERVER_URL);
    private static String authToken;
    enum State {loggedIn, loggedOut}
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

            else if (userInput.equalsIgnoreCase("register") || userInput.equalsIgnoreCase("r"))
            {
                register();
            }

            else if (userInput.equalsIgnoreCase("log in") || userInput.equalsIgnoreCase("l"))
            {
                login();
            }

            else if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("q"))
            {
                System.out.println("Thanks for playing! We hope to see you again soon.");
                System.exit(0);
            }

            else if (userInput.isEmpty())
            {
                System.out.println("User input empty. Type [H]elp to get started.");
            }

            else
            {
                System.out.println("Invalid input. Type [H]elp to get started or [Q]uit to exit.");
            }
        }

        if (currentState == State.loggedIn)
        {
            loggedInUI();
        }
    }

    public static void help()
    {
        System.out.println("What would you like to do?");
        System.out.println("1. [R]egister");
        System.out.println("2. [L]og In");
        System.out.println("3. [Q]uit");
    }

    private static void register()
    {
        System.out.print("Username: ");
        var username = SCANNER.nextLine();

        System.out.print("Password: ");
        var password = SCANNER.nextLine();

        System.out.print("Email: ");
        var email = SCANNER.nextLine();

        if (username.contains(" ") || password.contains(" ") || email.contains(" "))
        {
            System.out.println("Invalid input. Please try again.");
            register();
            return;
        }

        if (password.equalsIgnoreCase("password"))
        {
            System.out.println("Provided password is too weak. Please try again.");
            register();
            return;
        }

        if (!email.contains(".") || !email.contains("@"))
        {
            System.out.println("Invalid email. Please try again.");
            register();
            return;
        }

        try
        {
            RegisterResult result = SERVER_FACADE.register(username, password, email);
            authToken = result.authToken();
            currentState = State.loggedIn;
            System.out.println("Welcome to chess 240, " + username + "!");
        }
        catch (Exception exception)
        {
            System.out.println("Registration failed: " + exception.getMessage());
        }
    }

    private static void login()
    {
        System.out.print("Username: ");
        var username = SCANNER.nextLine().trim();

        System.out.print("Password: ");
        var password = SCANNER.nextLine();

        try {
            LoginResult result = SERVER_FACADE.login(username, password);
            authToken = result.authToken();
            currentState = State.loggedIn;
            System.out.println("Welcome back, " + username + "!");
        }
        catch (Exception exception)
        {
            System.out.println("Login failed: " + exception.getMessage());
        }
    }

      private static void loggedInUI()
    {
        while (currentState == State.loggedIn)
        {
            System.out.println("You are now logged in. Type [H]elp for options");
            String userInput = SCANNER.nextLine();

            if (userInput.equalsIgnoreCase("help") || userInput.equalsIgnoreCase("h"))
            {
                help_logged_in();
            }
            else
            {
                System.out.println("Invalid input. Type [H]elp for options or [Q]uit to exit.");
                if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("q"))
                {
                    System.out.println("Thanks for playing! We hope to see you again soon.");
                    System.exit(0);
                }
                loggedInUI();
            }
        }
    }

    private static void logout()
    {
        if (authToken == null)
        {
            currentState = State.loggedOut;
            return;
        }

        try
        {
            SERVER_FACADE.logout(authToken);
        }
        catch (Exception exception)
        {
            System.out.println("Logout failed." + exception.getMessage());
        }
        currentState = State.loggedOut;
        authToken = null;
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
            logout();
        }

        else if (userInput.equalsIgnoreCase("create game") || userInput.equalsIgnoreCase("c"))
        {
            System.out.println("New game name: ");
            String new_game_name = SCANNER.nextLine().trim();
            try
            {
                SERVER_FACADE.createGame(authToken, new_game_name);
            }
            catch (Exception exception)
            {
                System.out.println("Unable to create game." + exception.getMessage());
            }
        }

        else if (userInput.equalsIgnoreCase("list games") || userInput.equalsIgnoreCase("li"))
        {
            try
            {
                var games = SERVER_FACADE.listGames(authToken);
                games.games().forEach(game -> System.out.println(game.gameID() + ": " + game.gameName()));
            }
            catch (Exception exception)
            {
                System.out.println("Unable to list games." + exception.getMessage());
            }

        }

        else if (userInput.equalsIgnoreCase("play game") || userInput.equalsIgnoreCase("p"))
        {
            System.out.println("Which game would you like to join?: ");
            String game_id = SCANNER.nextLine().trim();
            System.out.println("Which color would you like to be? [W]hite or [B]lack?");
            String color = SCANNER.nextLine().trim();
            ChessGame.TeamColor teamColor = color.equalsIgnoreCase("w") ? WHITE : BLACK;
            try
            {
                SERVER_FACADE.joinGame(authToken, Integer.parseInt(game_id), teamColor);
                drawBoard();
            }
            catch (Exception exception)
            {
                System.out.println("Unable to join game." + exception.getMessage());
            }

        }

        else if (userInput.equalsIgnoreCase("observe game") || userInput.equalsIgnoreCase("o"))
        {
            System.out.println("Which game would you like to observe?: ");
            String game_id = SCANNER.nextLine().trim();
            try
            {
                SERVER_FACADE.observeGame(authToken, Integer.parseInt(game_id));
                drawBoard();
            }
            catch (Exception exception)
            {
                System.out.println("Unable to observe game." + exception.getMessage());
            }
        }

        else
        {
            System.out.println("Invalid input.");
            help_logged_in();
        }
    }

    private static void drawBoard()
    {
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//        DrawBoard bored = new DrawBoard();
//        bored.displayBoard2(board);
    }
}

