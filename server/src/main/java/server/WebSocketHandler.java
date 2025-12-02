package server;
import chess.ChessGame.TeamColor;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsErrorContext;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import static chess.ChessGame.TeamColor.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;


public class WebSocketHandler
{
    private final DataAccess dataAccess;
    private final Gson gson = new Gson();
    private final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WsMessageContext, Connection> sessionToConnection = new ConcurrentHashMap<>();

    private static class Connection
    {
        WsMessageContext context;
        String username;
        Integer gameID;
        TeamColor playerColor;

        Connection(WsMessageContext context, String username, Integer gameID, TeamColor playerColor)
        {
            this.context = context;
            this.username = username;
            this.gameID = gameID;
            this.playerColor = playerColor;
        }
    }

    public WebSocketHandler(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }

    public void onConnect(WsConnectContext context)
    {
        context.enableAutomaticPings();
    }

    public void onMessage(WsMessageContext context)
    {
        try
        {
            String message = context.message();
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String authToken = command.getAuthToken();
            Integer gameID = command.getGameID();

            AuthData authData = dataAccess.getAuth(authToken);
            if (authData == null)
            {
                sendError(context, "Error: unauthorized");
                return;
            }

            String username = authData.username();

            switch (command.getCommandType())
            {
                case CONNECT -> connect(context, username, gameID);
                case MAKE_MOVE -> makeMove(context, username, gameID, command.getMove());
                case LEAVE -> leave(context, username, gameID);
                case RESIGN -> resign(context, username, gameID);
            }
        }
        catch (Exception e)
        {
            sendError(context, "Error: " + e.getMessage());
        }
    }

    private void connect(WsMessageContext context, String username, Integer gameID) throws Exception
    {
        GameData game = dataAccess.getGame(gameID);
        if (game == null)
        {
            sendError(context, "Error: bad request.");
            return;
        }

        TeamColor playerColor = null;
        if (username.equals(game.whiteUsername()))
        {
            playerColor = WHITE;
        }
        else if (username.equals(game.blackUsername()))
        {
            playerColor = BLACK;
        }

        Connection connection = new Connection(context, username, gameID, playerColor);
        connections.computeIfAbsent(gameID, k -> new ArrayList<>()).add(connection);
        sessionToConnection.put(context, connection);

        ServerMessage loadGameMessage = new ServerMessage(LOAD_GAME);
        loadGameMessage.setGame(game.game());
        sendMessage(context, loadGameMessage);

        String notificationText;
        if (playerColor != null)
        {
            notificationText = username + " joined the game as " + playerColor + "!";
        }
        else
        {
            notificationText = username + " joined the game as an observer.";
        }
        broadcastToOthers(context, gameID, createNotification(notificationText));
    }

    private void makeMove(WsMessageContext context, String username, Integer gameID, ChessMove move)
    {
        Connection connection = sessionToConnection.get(context);
        if (connection == null || !connection.gameID.equals(gameID))
        {
            sendError(context, "Error: unauthorized.");
            return;
        }

        GameData game = dataAccess.getGame(gameID);
        if (game == null)
        {
            sendError(context, "Error: bad request.");
            return;
        }

        if (game.game() == null)
        {
            sendError(context, "Error: game is over.");
            return;
        }

        TeamColor currentTurn = game.game().getTeamTurn();
        if (connection.playerColor == null || connection.playerColor != currentTurn)
        {
            sendError(context, "Error: not your turn.");
            return;
        }

        if (!username.equals(game.whiteUsername()) && currentTurn == WHITE ||
                !username.equals(game.blackUsername()) && currentTurn == BLACK)
        {
            sendError(context, "Error: unauthorized.");
            return;
        }

        try
        {
            game.game().makeMove(move);

            dataAccess.updateGame(game);

            String moveDescription = describeMove(move, username);

            ServerMessage loadGameMessage = new ServerMessage(LOAD_GAME);
            loadGameMessage.setGame(game.game());
            broadcastToAll(gameID, loadGameMessage);

            broadcastToOthers(context, gameID, createNotification(moveDescription));

            TeamColor nextTurn = game.game().getTeamTurn();
            if (game.game().isInCheckmate(nextTurn))
            {
                broadcastToAll(gameID, createNotification(getPlayerName(game, nextTurn) +
                        " is in checkmate!\n------------------------\n|          Game Over!          | \n------------------------"));
                GameData endedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null);
                dataAccess.updateGame(endedGame);
            }
            else if (game.game().isInStalemate(nextTurn))
            {
                broadcastToAll(gameID, createNotification(getPlayerName(game, nextTurn) +
                        " is in stalemate!\n------------------------\n|          Game Over!          | \n------------------------"));
                GameData endedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null);
                dataAccess.updateGame(endedGame);
            }
            else if (game.game().isInCheck(nextTurn))
            {
                broadcastToAll(gameID, createNotification(getPlayerName(game, nextTurn) + " is in check!"));
            }
        }
        catch (Exception e)
        {
            sendError(context, "Error: invalid move");
        }
    }

    private void leave(WsMessageContext context, String username, Integer gameID) throws DataAccessException
    {
        Connection connection = sessionToConnection.get(context);
        if (connection == null || !connection.gameID.equals(gameID))
        {
            sendError(context, "Error: unauthorized.");
            return;
        }

        helperFunction(gameID, connection);
        removeConnection(context, gameID);
        broadcastToOthers(context, gameID, createNotification(username + " left the game!"));
    }

    private void helperFunction(Integer gameID, Connection connection) throws DataAccessException {
        if (connection.playerColor != null)
        {
            GameData game = dataAccess.getGame(gameID);
            if (game != null)
            {
                GameData updatedGame;
                if (connection.playerColor == WHITE)
                {
                    updatedGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                }
                else
                {
                    updatedGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                }
                dataAccess.updateGame(updatedGame);
            }
        }
    }

    private void resign(WsMessageContext context, String username, Integer gameID) throws Exception
    {
        Connection connection = sessionToConnection.get(context);
        if (connection == null || !connection.gameID.equals(gameID) || connection.playerColor == null)
        {
            sendError(context, "Error: unauthorized.");
            return;
        }

        GameData game = dataAccess.getGame(gameID);
        if (game == null)
        {
            sendError(context, "Error: bad request.");
            return;
        }

        if (game.game() == null)
        {
            sendError(context, "Error: game is over.");
            return;
        }

        GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null);
        dataAccess.updateGame(updatedGame);

        broadcastToAll(gameID, createNotification(username + " resigned the game!"));
    }

    private void sendError(WsMessageContext context, String errorMessage)
    {
        try
        {
            ServerMessage errorMsg = new ServerMessage(ERROR);
            errorMsg.setErrorMessage(errorMessage);
            sendMessage(context, errorMsg);
        }
        catch (Exception ignored) {}
    }

    private ServerMessage createNotification(String message)
    {
        ServerMessage notification = new ServerMessage(NOTIFICATION);
        notification.setMessage(message);
        return notification;
    }

    private void sendMessage(WsMessageContext context, ServerMessage message) throws Exception
    {
        try
        {
            context.send(gson.toJson(message));
        }
        catch (Exception e)
        {
            throw new Exception(e);
        }
    }

    private void broadcastToOthers(WsMessageContext excludeContext, Integer gameID, ServerMessage message)
    {
        ArrayList<Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null)
        {
            for (Connection connection : new ArrayList<>(gameConnections))
            {
                if (connection != null && connection.context != null && !connection.context.equals(excludeContext))
                {
                    try
                    {
                        sendMessage(connection.context, message);
                    }
                    catch (Exception e)
                    {
                        removeConnection(connection.context, gameID);
                    }
                }
            }
        }
    }

    private void broadcastToAll(Integer gameID, ServerMessage message)
    {
        ArrayList<Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null)
        {
            for (Connection connection : new ArrayList<>(gameConnections))
            {
                if (connection != null && connection.context != null)
                {
                    try
                    {
                        sendMessage(connection.context, message);
                    }
                    catch (Exception e)
                    {
                        removeConnection(connection.context, gameID);
                    }
                }
            }
        }
    }

    private void removeConnection(WsMessageContext context, Integer gameID)
    {
        Connection connection = sessionToConnection.remove(context);
        if (connection != null)
        {
            ArrayList<Connection> gameConnections = connections.get(gameID);
            if (gameConnections != null)
            {
                gameConnections.remove(connection);
                if (gameConnections.isEmpty())
                {
                    connections.remove(gameID);
                }
            }
        }
    }

    private String describeMove(ChessMove move, String username)
    {
        return username + " made a move: " + positionToString(move.getStartPosition()) + " -> " + positionToString(move.getEndPosition()) + ".";
    }

    private String positionToString(ChessPosition pos)
    {
        char col = (char)('a' + pos.getColumn() - 1);
        return col + String.valueOf(pos.getRow());
    }

    private String getPlayerName(GameData game, TeamColor color)
    {
        if (color == WHITE)
        {
            return game.whiteUsername() != null ? game.whiteUsername() : "White";
        }
        else
        {
            return game.blackUsername() != null ? game.blackUsername() : "Black";
        }
    }

    public void onClose(WsCloseContext context)
    {
        Integer gameIDToRemove = null;
        Connection toRemove = null;
        for (var entry : sessionToConnection.entrySet())
        {
            WsMessageContext messageContext = entry.getKey();
            if (messageContext.sessionId().equals(context.sessionId()))
            {
                toRemove = entry.getValue();
                gameIDToRemove = toRemove.gameID;
                break;
            }
        }
        if (toRemove != null && gameIDToRemove != null)
        {
            try
            {
                helperFunction(gameIDToRemove, toRemove);
            }
            catch (DataAccessException ignored) {}
            removeConnection(toRemove.context, gameIDToRemove);
        }
    }

    public void onError(WsErrorContext context)
    {
        Connection connection = null;
        for (var entry : sessionToConnection.entrySet())
        {
            WsMessageContext messageContext = entry.getKey();
            if (messageContext.sessionId().equals(context.sessionId()))
            {
                connection = entry.getValue();
                break;
            }
        }
        if (connection != null)
        {
            assert context.error() != null;
            sendError(connection.context, "Error: " + context.error().getMessage());
        }
    }
}

