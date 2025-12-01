package server;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler
{
    private final DataAccess dataAccess;
    private final Gson gson = new Gson();
    private final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WsMessageContext, Connection> sessionToConnection = new ConcurrentHashMap<>();

    private static class Connection
    {
        WsMessageContext ctx;
        String username;
        Integer gameID;
        ChessGame.TeamColor playerColor;

        Connection(WsMessageContext ctx, String username, Integer gameID, ChessGame.TeamColor playerColor)
        {
            this.ctx = ctx;
            this.username = username;
            this.gameID = gameID;
            this.playerColor = playerColor;
        }
    }

    public WebSocketHandler(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }

    public void onConnect(WsConnectContext ctx)
    {
        ctx.enableAutomaticPings();
    }

    public void onMessage(WsMessageContext ctx)
    {
        try
        {
            String message = ctx.message();
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String authToken = command.getAuthToken();
            Integer gameID = command.getGameID();

            AuthData authData = dataAccess.getAuth(authToken);
            if (authData == null)
            {
                sendError(ctx, "Error: unauthorized");
                return;
            }

            String username = authData.username();

            switch (command.getCommandType())
            {
                case CONNECT -> connect(ctx, username, gameID);
                case MAKE_MOVE -> makeMove(ctx, username, gameID, command.getMove());
                case LEAVE -> leave(ctx, username, gameID);
                case RESIGN -> resign(ctx, username, gameID);
            }
        }
        catch (Exception e)
        {
            sendError(ctx, "Error: " + e.getMessage());
        }
    }

    private void connect(WsMessageContext ctx, String username, Integer gameID) throws IOException {
        GameData game = dataAccess.getGame(gameID);
        if (game == null)
        {
            sendError(ctx, "Error: bad request.");
            return;
        }

        ChessGame.TeamColor playerColor = null;
        if (username.equals(game.whiteUsername()))
        {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        else if (username.equals(game.blackUsername()))
        {
            playerColor = ChessGame.TeamColor.BLACK;
        }

        Connection connection = new Connection(ctx, username, gameID, playerColor);
        connections.computeIfAbsent(gameID, k -> new ArrayList<>()).add(connection);
        sessionToConnection.put(ctx, connection);

        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameMessage.setGame(game.game());
        sendMessage(ctx, loadGameMessage);

        String notificationText;
        if (playerColor != null)
        {
            notificationText = username + " joined the game as " + playerColor + ".";
        }
        else
        {
            notificationText = username + " joined the game as an observer.";
        }
        broadcastToOthers(ctx, gameID, createNotification(notificationText));
    }

    private void makeMove(WsMessageContext ctx, String username, Integer gameID, ChessMove move) throws DataAccessException
    {
        Connection connection = sessionToConnection.get(ctx);
        if (connection == null || !connection.gameID.equals(gameID))
        {
            sendError(ctx, "Error: unauthorized.");
            return;
        }

        GameData game = dataAccess.getGame(gameID);
        if (game == null)
        {
            sendError(ctx, "Error: bad request.");
            return;
        }

        if (game.game() == null)
        {
            sendError(ctx, "Error: game is over.");
            return;
        }

        ChessGame.TeamColor currentTurn = game.game().getTeamTurn();
        if (connection.playerColor == null || connection.playerColor != currentTurn)
        {
            sendError(ctx, "Error: not your turn");
            return;
        }

        if (!username.equals(game.whiteUsername()) && currentTurn == ChessGame.TeamColor.WHITE ||
                !username.equals(game.blackUsername()) && currentTurn == ChessGame.TeamColor.BLACK)
        {
            sendError(ctx, "Error: unauthorized");
            return;
        }

        try
        {
            game.game().makeMove(move);

            dataAccess.updateGame(game);

            String moveDescription = describeMove(move, username);

            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGameMessage.setGame(game.game());
            broadcastToAll(gameID, loadGameMessage);

            broadcastToAll(gameID, createNotification(moveDescription));

            ChessGame.TeamColor nextTurn = game.game().getTeamTurn();
            if (game.game().isInCheckmate(nextTurn))
            {
                broadcastToAll(gameID, createNotification(getPlayerName(game, nextTurn) + " is in checkmate!\n Game Over!"));
                GameData endedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null);
                dataAccess.updateGame(endedGame);
            }
            else if (game.game().isInStalemate(nextTurn))
            {
                broadcastToAll(gameID, createNotification(getPlayerName(game, nextTurn) + " is in stalemate!\n Game Over!"));
                GameData endedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null);
                dataAccess.updateGame(endedGame);
            }
            else if (game.game().isInCheck(nextTurn))
            {
                broadcastToAll(gameID, createNotification(getPlayerName(game, nextTurn) + " is in check!"));
            }

        }
        catch (InvalidMoveException e)
        {
            sendError(ctx, "Error: invalid move");
        }
    }

    private void leave(WsMessageContext ctx, String username, Integer gameID) throws DataAccessException
    {
        Connection connection = sessionToConnection.get(ctx);
        if (connection == null || !connection.gameID.equals(gameID))
        {
            sendError(ctx, "Error: unauthorized");
            return;
        }

        if (connection.playerColor != null)
        {
            GameData game = dataAccess.getGame(gameID);
            if (game != null)
            {
                GameData updatedGame;
                if (connection.playerColor == ChessGame.TeamColor.WHITE)
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

        removeConnection(ctx, gameID);

        broadcastToOthers(ctx, gameID, createNotification(username + " left the game!"));
    }

    private void resign(WsMessageContext ctx, String username, Integer gameID) throws DataAccessException
    {
        Connection connection = sessionToConnection.get(ctx);
        if (connection == null || !connection.gameID.equals(gameID) || connection.playerColor == null)
        {
            sendError(ctx, "Error: unauthorized");
            return;
        }

        GameData game = dataAccess.getGame(gameID);
        if (game == null)
        {
            sendError(ctx, "Error: bad request");
            return;
        }

        GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null);
        dataAccess.updateGame(updatedGame);

        broadcastToAll(gameID, createNotification(username + " resigned the game!"));
    }

    private void sendError(WsMessageContext ctx, String errorMessage)
    {
        try
        {
            ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMsg.setErrorMessage(errorMessage);
            sendMessage(ctx, errorMsg);
        }
        catch (IOException e) {}
    }

    private ServerMessage createNotification(String message)
    {
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(message);
        return notification;
    }

    private void sendMessage(WsMessageContext ctx, ServerMessage message) throws IOException
    {
        try
        {
            ctx.send(gson.toJson(message));
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    private void broadcastToAll(Integer gameID, ServerMessage message)
    {
        ArrayList<Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null)
        {
            for (Connection conn : new ArrayList<>(gameConnections))
            {
                try
                {
                    sendMessage(conn.ctx, message);
                }
                catch (Exception e)
                {
                    removeConnection(conn.ctx, gameID);
                }
            }
        }
    }

    private void broadcastToOthers(WsMessageContext excludeCtx, Integer gameID, ServerMessage message)
    {
        ArrayList<Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null)
        {
            for (Connection conn : new ArrayList<>(gameConnections))
            {
                if (!conn.ctx.equals(excludeCtx))
                {
                    try
                    {
                        sendMessage(conn.ctx, message);
                    }
                    catch (Exception e)
                    {
                        removeConnection(conn.ctx, gameID);
                    }
                }
            }
        }
    }

    private void removeConnection(WsMessageContext ctx, Integer gameID)
    {
        Connection connection = sessionToConnection.remove(ctx);
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
        return username + " moved from " + positionToString(move.getStartPosition()) + " to " + positionToString(move.getEndPosition()) + ".";
    }

    private String positionToString(chess.ChessPosition pos)
    {
        char col = (char)('a' + pos.getColumn() - 1);
        return col + String.valueOf(pos.getRow());
    }

    private String getPlayerName(GameData game, ChessGame.TeamColor color)
    {
        if (color == ChessGame.TeamColor.WHITE)
        {
            return game.whiteUsername() != null ? game.whiteUsername() : "White";
        }
        else
        {
            return game.blackUsername() != null ? game.blackUsername() : "Black";
        }
    }

    public void onClose(WsCloseContext ctx)
    {
        Connection toRemove = null;
        Integer gameIDToRemove = null;
        for (var entry : sessionToConnection.entrySet())
        {
            WsMessageContext msgCtx = entry.getKey();
            if (msgCtx.sessionId().equals(ctx.sessionId()))
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
                if (toRemove.playerColor != null)
                {
                    GameData game = dataAccess.getGame(gameIDToRemove);
                    if (game != null)
                    {
                        GameData updatedGame;
                        if (toRemove.playerColor == ChessGame.TeamColor.WHITE)
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
            catch (DataAccessException e) {}
            removeConnection(toRemove.ctx, gameIDToRemove);
        }
    }

    public void onError(WsErrorContext ctx)
    {
        Connection connection = null;
        for (var entry : sessionToConnection.entrySet())
        {
            WsMessageContext msgCtx = entry.getKey();
            if (msgCtx.sessionId().equals(ctx.sessionId()))
            {
                connection = entry.getValue();
                break;
            }
        }
        if (connection != null)
        {
            sendError(connection.ctx, "Error: " + ctx.error().getMessage());
        }
    }
}
