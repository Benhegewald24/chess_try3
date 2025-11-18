import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;
import model.GameData;
import results.LoginResult;
import results.RegisterResult;
import ui.DrawBoard;
import static chess.ChessGame.TeamColor.*;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String SERVER_URL = "http://localhost:8080";
    private static final ServerFacade SERVER_FACADE = new ServerFacade(SERVER_URL);
    private static String authToken;
    private static List<GameData> lastGamesList = new ArrayList<>();
    enum State {loggedIn, loggedOut}
    static State currentState = State.loggedOut;

    public static void main(String[] args) {
        System.out.println("Welcome to 240 chess. Type [H]elp to get started.");

        while (true) {
            if (currentState == State.loggedOut) {
                loggedOutUI();}

            else if (currentState == State.loggedIn) {
                loggedInUI();}}}

    public static void loggedOutUI() {
        while (currentState == State.loggedOut) {
            String userInput = SCANNER.nextLine().trim();

            if (userInput.equalsIgnoreCase("help") || userInput.equalsIgnoreCase("h")) {
                help();}

            else if (userInput.equalsIgnoreCase("register") || userInput.equalsIgnoreCase("r")) {
                register();}

            else if (userInput.equalsIgnoreCase("log in") || userInput.equalsIgnoreCase("l")) {
                login();}

            else if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("q")) {
                System.out.println("Thanks for playing! We hope to see you again soon.");
                System.exit(0);}

            else if (userInput.isEmpty()) {
                System.out.println("User input empty. Type [H]elp to get started.");}

            else {
                System.out.println("Invalid input. Type [H]elp to get started or [Q]uit to exit.");}}}

    public static void help() {
        System.out.println("\nWhat would you like to do?\n1. [R]egister\n2. [L]og In\n3. [Q]uit\n4. [H]elp");}

    private static void register() {
        System.out.print("\nUsername: ");
        String username = SCANNER.nextLine().trim();

        System.out.print("Password: ");
        String password = SCANNER.nextLine().trim();

        System.out.print("Email: ");
        String email = SCANNER.nextLine().trim();

        if (username.contains(" ") || password.contains(" ") || email.contains(" ") || username.isBlank())
        {
            System.out.println("Invalid input. Please try again.");
            help();
            return;}

        if (password.equalsIgnoreCase("password")) {
            System.out.println("Provided password is too weak. Please try again.");
            register();
            return;}

        if (!email.contains(".") || !email.contains("@")) {
            System.out.println("Invalid email. Please try again.");
            register();
            return;}

        try {
            RegisterResult result = SERVER_FACADE.register(username, password, email);
            authToken = result.authToken();
            currentState = State.loggedIn;
            System.out.println("\nWelcome to chess 240, " + username + "!");}
        catch (Exception exception) {
            String errorMessage = exception.getMessage();
            if (errorMessage == null || errorMessage.isBlank()) {
                errorMessage = "Unable to connect to server. Please ensure the server is running.";}
            System.out.println("Registration " + errorMessage);
            help();}}

    private static void login() {
        System.out.print("\nUsername: ");
        String username = SCANNER.nextLine().trim();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        try {
            LoginResult result = SERVER_FACADE.login(username, password);
            authToken = result.authToken();
            currentState = State.loggedIn;
            System.out.println("\nWelcome back, " + username + "!");}
        catch (Exception exception) {
            String errorMessage = exception.getMessage();
            if (errorMessage == null || errorMessage.isBlank()) {
                errorMessage = "Unable to connect to server. Please ensure the server is running.";}
            System.out.println("\nLogin failed: " + errorMessage);
            help();}}

    private static void loggedInUI() {
        while (currentState == State.loggedIn) {
            System.out.println("You are logged in. What would you like to do next? Type [H]elp for options");
            String userInput = SCANNER.nextLine().trim();

            if (userInput.equalsIgnoreCase("help") || userInput.equalsIgnoreCase("h")) {
                helpLoggedIn();}
            else if (userInput.equalsIgnoreCase("log out") || userInput.equalsIgnoreCase("lo")) {
                logout();}
            else if (userInput.equalsIgnoreCase("create game") || userInput.equalsIgnoreCase("c") || userInput.equalsIgnoreCase("create")) {
                createGameHelper();}
            else if (userInput.equalsIgnoreCase("list games") || userInput.equalsIgnoreCase("li") || userInput.equalsIgnoreCase("list")) {
                try {
                    var games = SERVER_FACADE.listGames(authToken);
                    lastGamesList = new ArrayList<>(games.games());
                    int counter = 1;
                    for (GameData game : lastGamesList) {
                        System.out.println(counter + ". " + game.gameName());
                        counter++;}}
                catch (Exception exception) {
                    System.out.println("Unable to list games." + exception.getMessage());}}
            else if (userInput.equalsIgnoreCase("play game") || userInput.equalsIgnoreCase("p") || userInput.equalsIgnoreCase("play")) {
                System.out.print("Which game would you like to join? (Enter Game #): ");
                String gameNum = SCANNER.nextLine().trim();
                System.out.print("Which color would you like to be? [W]hite or [B]lack?: ");
                String color = SCANNER.nextLine().trim();
                ChessGame.TeamColor teamColor = color.equalsIgnoreCase("b") ? BLACK : WHITE;
                try {
                    listGameHelper(gameNum, teamColor);}
                catch (Exception exception) {
                    String errorMessage = exception.getMessage();
                    if (errorMessage != null && errorMessage.contains("already taken")) {
                        System.out.println("\nColor already taken for this game.");
                        continue;}
                    System.out.println("Unable to join game. " + errorMessage);}}
            else if (userInput.equalsIgnoreCase("observe game") || userInput.equalsIgnoreCase("observe") || userInput.equalsIgnoreCase("o")) {
                System.out.print("Which game would you like to observe? (Enter Game #): ");
                String gameNum = SCANNER.nextLine().trim();
                try {
                    var games = SERVER_FACADE.listGames(authToken);
                    lastGamesList = new ArrayList<>(games.games());
                    int gameNumber = Integer.parseInt(gameNum);
                    if (gameNumber < 1 || gameNumber > lastGamesList.size()) {
                        System.out.println("Invalid game number.\n");
                        continue;
                    }
                    GameData selectedGame = lastGamesList.get(gameNumber - 1);
                    System.out.println("White Pieces: " + selectedGame.whiteUsername() + "   |   Black Pieces: " + selectedGame.blackUsername());
                    SERVER_FACADE.observeGame(authToken, selectedGame.gameID());
                    drawBoard(WHITE);
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid game number. Please enter a number.\n");
                }
                catch (Exception exception) {
                    System.out.println("Unable to observe game. " + exception.getMessage());}}
            else if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("q")) {
                System.out.println("Thanks for playing! We hope to see you again soon.");
                System.exit(0);}
            else {
                System.out.println("Invalid input. Type [H]elp for options or [Q]uit to exit.");}}}

    private static void logout() {
        if (authToken == null) {
            currentState = State.loggedOut;
            return;}

        try {
            SERVER_FACADE.logout(authToken);
            System.out.println("\nUser logged out.");}
        catch (Exception exception) {
            System.out.println("Logout failed." + exception.getMessage());}
        currentState = State.loggedOut;
        authToken = null;
        help();}

    public static void helpLoggedIn() {
        System.out.println("\nYou are logged in. What would you like to do?");
        System.out.println("1. [Lo]g Out\n2. [C]reate Game\n3. [Li]st Games\n4. [P]lay Game\n5. [O]bserve Game\n6. [H]elp");
        String userInput = SCANNER.nextLine().trim();

        if (userInput.equalsIgnoreCase("log out") || userInput.equalsIgnoreCase("lo") || userInput.equals("1")) {logout();}

        else if (userInput.equalsIgnoreCase("create game") || userInput.equalsIgnoreCase("c") || userInput.equals("2")) {
            createGameHelper();}

        else if (userInput.equalsIgnoreCase("list games") ||
                userInput.equalsIgnoreCase("li") || userInput.equalsIgnoreCase("list") || userInput.equals("3")) {
            try {
                var games = SERVER_FACADE.listGames(authToken);
                lastGamesList = new ArrayList<>(games.games());
                int counter = 1;
                System.out.print("\n");
                for (GameData game : lastGamesList) {
                    System.out.print("- " + counter + ". " + game.gameName());
                    counter++;
                    int lengthOfGameName = game.gameName().length();
                    int helper = 17 - lengthOfGameName;
                    System.out.print(" ".repeat(helper));
                    System.out.print("White Pieces: " + game.whiteUsername());
                    if (game.whiteUsername() == null) {
                        System.out.print("        ");}
                    else {
                        int lengthOfWhiteUsername = game.whiteUsername().length();
                        int helper2 = 12 - lengthOfWhiteUsername;
                        System.out.print(" ".repeat(helper2));}
                    System.out.println("|        Black Pieces: " + game.blackUsername());}
                System.out.print("\n");}
            catch (Exception exception) {
                System.out.println("Unable to list games." + exception.getMessage());}}

        else if (userInput.equalsIgnoreCase("play game") || userInput.equalsIgnoreCase("p") ||
                userInput.equals("4") || userInput.equals("play")) {
            System.out.print("Which game would you like to join? (Enter Game #): ");
            String gameNum = SCANNER.nextLine().trim();
            System.out.print("Which color would you like to be? [W]hite or [B]lack?: ");
            String color = SCANNER.nextLine().trim();
            System.out.print("\n");
            ChessGame.TeamColor teamColor = color.equalsIgnoreCase("w") ? WHITE : BLACK;
            try {
                listGameHelper(gameNum, teamColor);}
            catch (Exception exception) {
                System.out.println("Unable to join game. " + exception.getMessage());}}

        else if (userInput.equalsIgnoreCase("observe game") || userInput.equals("5") || userInput.equals("observe") ||
                userInput.equalsIgnoreCase("o")) {
            System.out.print("Which game would you like to observe? (Enter Game #): ");
            String gameNum = SCANNER.nextLine().trim();
            try {
                var games = SERVER_FACADE.listGames(authToken);
                lastGamesList = new ArrayList<>(games.games());
                int gameNumber = Integer.parseInt(gameNum);
                if (gameNumber < 1 || gameNumber > lastGamesList.size()) {
                    System.out.println("Invalid game number.\n");
                    return;}
                GameData selectedGame = lastGamesList.get(gameNumber - 1);
                System.out.println("White Pieces: " + selectedGame.whiteUsername() + "     |     Black Pieces: " + selectedGame.blackUsername());
                SERVER_FACADE.observeGame(authToken, selectedGame.gameID());
                drawBoard(WHITE);}
            catch (NumberFormatException e) {
                System.out.println("Invalid game number. Please enter a number.\n");}
            catch (Exception exception) {
                System.out.println("Unable to observe game. " + exception.getMessage());}}

        else if (userInput.equalsIgnoreCase("h") || userInput.equalsIgnoreCase("help")) {
            helpLoggedIn();}

        else {
            if (!userInput.isEmpty()) {
                System.out.println("Invalid input. Please enter one of the options from the menu.");}}}

    private static void createGameHelper() {
        System.out.print("New game name: ");
        String newGameName = SCANNER.nextLine().trim();
        try {
            SERVER_FACADE.createGame(authToken, newGameName);}
        catch (Exception exception) {
            System.out.println("Unable to create game." + exception.getMessage()); }}

    private static void listGameHelper(String gameNum, ChessGame.TeamColor teamColor) throws Exception {
        var games = SERVER_FACADE.listGames(authToken);
        lastGamesList = new ArrayList<>(games.games());
        int gameNumber = Integer.parseInt(gameNum);
        if (gameNumber < 1 || gameNumber > lastGamesList.size()) {
            System.out.println("Invalid game number.\n");
            return;}
        GameData selectedGame = lastGamesList.get(gameNumber - 1);
        SERVER_FACADE.joinGame(authToken, selectedGame.gameID(), teamColor);
        drawBoard(teamColor);}

    private static void drawBoard(ChessGame.TeamColor teamColor) {
        DrawBoard bored = new DrawBoard();
        bored.displayBoard(teamColor);
        boolean switcher = false;
        if (switcher) {
            bored.displayBoard2(new ChessBoard());}}}