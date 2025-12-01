import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import chess.*;
import client.ServerFacade;
import client.WebSocketClient;
import model.GameData;
import results.LoginResult;
import results.RegisterResult;
import ui.DrawBoard;
import websocket.messages.ServerMessage;
import static chess.ChessGame.TeamColor.*;

public class Main
{
    enum State
    {
        loggedIn, loggedOut, inGame
    }

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String SERVER_URL = "http://localhost:8080";
    private static final ServerFacade SERVER_FACADE = new ServerFacade(SERVER_URL);
    private static String authToken;
    private static List<GameData> lastGamesList = new ArrayList<>();
    private static WebSocketClient webSocketClient;
    private static ChessGame currentGame;
    private static ChessGame.TeamColor playerColor = null;
    private static Integer currentGameID;
    static State currentState = State.loggedOut;
    private static volatile boolean waitingForMoveResponse = false;

    public static void main(String[] args) {
        System.out.println("Welcome to 240 chess. Type [H]elp to get started.");

        while (true) {
            if (currentState == State.loggedOut) {
                loggedOutUI();}
            else if (currentState == State.loggedIn) {
                loggedInUI();}
            else
            {
                gameplayUI();
            }
        }
    }

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
                System.out.println("\nInvalid input. Type [H]elp to get started or [Q]uit to exit.");}}}

    public static void help() {
        System.out.println("\nWhat would you like to do?\n1. [R]egister\n2. [L]og In\n3. [Q]uit\n4. [H]elp");}

    private static void register() {
        System.out.print("\nUsername: ");
        String username = SCANNER.nextLine().trim();

        System.out.print("Password: ");
        String password = SCANNER.nextLine().trim();

        System.out.print("Email: ");
        String email = SCANNER.nextLine().trim();

        if (username.contains(" ") || password.contains(" ") || email.contains(" ") || username.isBlank() || password.isBlank())
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
            System.out.println("\nRegistration " + errorMessage);
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
            System.out.println("\nYou are logged in. What would you like to do next? Type [H]elp.");
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
                if (!color.equalsIgnoreCase("w") && !color.equalsIgnoreCase("white") &&
                    !color.equalsIgnoreCase("b") && !color.equalsIgnoreCase("black")) {
                    System.out.println("\nInvalid color. Please enter [W]hite or [B]lack.");
                    continue;
                }
                ChessGame.TeamColor teamColor = color.equalsIgnoreCase("b") || color.equalsIgnoreCase("black") ? BLACK : WHITE;
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
                currentGameID = selectedGame.gameID();
                playerColor = null;
                setupWebSocket(selectedGame.gameID(), null);
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
            if (webSocketClient != null && webSocketClient.isConnected())
            {
                if (currentGameID != null)
                {
                    webSocketClient.sendLeave(authToken, currentGameID);
                }
                webSocketClient.disconnect();
            }
            SERVER_FACADE.logout(authToken);
            System.out.println("\nUser logged out.");}
        catch (Exception exception) {
            System.out.println("Logout failed." + exception.getMessage());}
        currentState = State.loggedOut;
        authToken = null;
        currentGame = null;
        currentGameID = null;
        playerColor = null;
        webSocketClient = null;
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
                    int helper = 20 - lengthOfGameName;
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
            if (!color.equalsIgnoreCase("w") && !color.equalsIgnoreCase("white") &&
                !color.equalsIgnoreCase("b") && !color.equalsIgnoreCase("black")) {
                System.out.println("\nInvalid color. Please enter [W]hite or [B]lack.");
                return;
            }
            System.out.print("\n");
            ChessGame.TeamColor teamColor = color.equalsIgnoreCase("b") || color.equalsIgnoreCase("black") ? BLACK : WHITE;
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
                currentGameID = selectedGame.gameID();
                playerColor = null;
                setupWebSocket(selectedGame.gameID(), null);}
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
        System.out.print("\nNew game name: ");
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
        currentGameID = selectedGame.gameID();
        playerColor = teamColor;
        setupWebSocket(selectedGame.gameID(), playerColor);}

    private static void setupWebSocket(Integer gameID, ChessGame.TeamColor playerColor) throws Exception
    {
        webSocketClient = new WebSocketClient(SERVER_URL);
        webSocketClient.setMessageHandler(Main::handleServerMessage);
        webSocketClient.connect();
        webSocketClient.sendConnect(authToken, gameID);
        currentState = State.inGame;
    }

    private static void handleServerMessage(ServerMessage message)
    {
        switch (message.getServerMessageType())
        {
            case LOAD_GAME:
                currentGame = message.getGame();
                if (currentGame != null)
                {
                    drawGameBoard();}
                if (waitingForMoveResponse)
                {
                    waitingForMoveResponse = false;
                }
                break;
            case ERROR:
                System.out.println("\n" + message.getErrorMessage());
                if (waitingForMoveResponse)
                {
                    waitingForMoveResponse = false;
                }
                break;
            case NOTIFICATION:
                System.out.println("\n" + message.getMessage());
                break;
        }
    }

    private static void gameplayUI()
    {
        while (currentState == State.inGame)
        {
            System.out.println("\nWhat would you like to do? Type [H]elp for options.");
            String userInput = SCANNER.nextLine().trim();

            if (userInput.equalsIgnoreCase("help") || userInput.equalsIgnoreCase("h"))
            {
                gameplayHelp();}
            else if (userInput.equalsIgnoreCase("make move") || userInput.equalsIgnoreCase("m") || userInput.equalsIgnoreCase("make"))
            {
                if (playerColor == null)
                {
                    System.out.println("Observers cannot make moves.");
                    continue;
                }
                makeMove();}
            else if (userInput.equalsIgnoreCase("highlight") || userInput.equalsIgnoreCase("hi") || userInput.equalsIgnoreCase("highlight valid moves"))
            {
                highlightMoves();}
            else if (userInput.equalsIgnoreCase("redraw") || userInput.equalsIgnoreCase("red") || userInput.equalsIgnoreCase("redraw board"))
            {
                drawGameBoard();}
            else if (userInput.equalsIgnoreCase("resign") || userInput.equalsIgnoreCase("res") || userInput.equalsIgnoreCase("resign game"))
            {
                if (playerColor == null)
                {
                    continue;
                }
                resign();}
            else if (userInput.equalsIgnoreCase("leave") || userInput.equalsIgnoreCase("l") || userInput.equalsIgnoreCase("leave game"))
            {
                leaveGame();}
        }
    }

    private static void gameplayHelp()
    {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. [Hi]ghlight Valid Moves");
        System.out.println("2. [Red]raw Board");
        System.out.println("3. [L]eave Game");
        System.out.println("4. [H]elp");
        if (playerColor != null)
        {
            System.out.println("5. [M]ake Move");
            System.out.println("6. [Res]ign Game");
        }
    }

    private static void makeMove()
    {
        try
        {
            System.out.print("Enter move: ");
            String moveInput = SCANNER.nextLine().trim();
            String[] parts = moveInput.split("\\s+");
            if (parts.length != 2)
            {
                System.out.println("Invalid move format.");
                return;}

            ChessPosition start = parsePosition(parts[0]);
            ChessPosition end = parsePosition(parts[1]);
            if (start == null || end == null)
            {
                System.out.println("Invalid position format.");
                return;}

            ChessMove move = new ChessMove(start, end, null);
            waitingForMoveResponse = true;
            webSocketClient.sendMakeMove(authToken, currentGameID, move);

            while (waitingForMoveResponse)
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        catch (Exception exception)
        {
            System.out.println("Error making move: " + exception.getMessage());
            waitingForMoveResponse = false;
        }
    }

    private static void highlightMoves()
    {
        if (currentGame == null)
        {
            System.out.println("No game loaded.");
            return;}

        System.out.print("Enter piece position to highlight: ");
        String posInput = SCANNER.nextLine().trim();
        ChessPosition pos = parsePosition(posInput);
        if (pos == null)
        {
            System.out.println("Invalid position format.");
            return;}

        var validMoves = currentGame.validMoves(pos);
        if (validMoves.isEmpty())
        {
            System.out.println("No valid moves for piece at " + posInput + "!");
            return;}

        java.util.Set<ChessPosition> highlightedPositions = new java.util.HashSet<>();
        highlightedPositions.add(pos);
        for (ChessMove move : validMoves)
        {
            highlightedPositions.add(move.getEndPosition());}

        ChessGame.TeamColor perspective = (playerColor != null) ? playerColor : ChessGame.TeamColor.WHITE;
        DrawBoard drawBoard = new DrawBoard();
        drawBoard.displayBoard(currentGame, perspective, highlightedPositions);
    }

    private static void resign()
    {
        System.out.print("Are you sure you want to resign? [Y]es or [N]o: ");
        String confirm = SCANNER.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y"))
        {
            try
            {
                webSocketClient.sendResign(authToken, currentGameID);
                System.out.println("Game Over!");}
            catch (Exception exception)
            {
                System.out.println("Error resigning: " + exception.getMessage());}
        }
    }

    private static void leaveGame()
    {
        System.out.print("Are you sure you want to leave? [Y]es or [N]o: ");
        String confirm = SCANNER.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y"))
        {
            try
            {
                if (webSocketClient != null && webSocketClient.isConnected())
                {
                    webSocketClient.sendLeave(authToken, currentGameID);
                    webSocketClient.disconnect();
                }
                currentState = State.loggedIn;
                currentGame = null;
                currentGameID = null;
                playerColor = null;
                System.out.println("You have left the game.");}
            catch (Exception exception)
            {
                System.out.println("Error leaving game: " + exception.getMessage());
                currentState = State.loggedIn;}
        }
    }

    private static ChessPosition parsePosition(String pos)
    {
        if (pos == null || pos.length() != 2)
        {
            return null;}

        char colChar = pos.charAt(0);
        char rowChar = pos.charAt(1);
        if (colChar < 'a' || colChar > 'h' || rowChar < '1' || rowChar > '8')
        {
            return null;}

        int col = colChar - 'a' + 1;
        int row = rowChar - '0';
        return new ChessPosition(row, col);
    }

    private static void drawGameBoard()
    {
        if (currentGame == null || currentGame.getBoard() == null)
        {
            System.out.println("Board unavailable.");
            return;
        }

        ChessGame.TeamColor viewColor = playerColor != null ? playerColor : WHITE;
        DrawBoard drawer = new DrawBoard();
        drawer.displayBoard(currentGame, viewColor);
    }
}
