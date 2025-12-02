package client;
import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import java.net.URI;
import java.util.function.Consumer;

import static websocket.commands.UserGameCommand.CommandType.*;

@ClientEndpoint
public class WebSocketClient
{
    private Session session;
    private final Gson gson = new Gson();
    private Consumer<ServerMessage> messageHandler;
    private final String serverUrl;

    public WebSocketClient(String serverUrl)
    {
        this.serverUrl = serverUrl.replace("http://", "ws://").replace("https://", "wss://");
    }

    public void connect() throws Exception
    {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(serverUrl + "/ws"));
    }

    public void disconnect() throws Exception
    {
        if (session != null && session.isOpen())
        {
            session.close();
        }
    }

    @OnOpen
    public void onOpen(Session session)
    {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message)
    {
        try
        {
            ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
            if (messageHandler != null)
            {
                messageHandler.accept(serverMessage);
            }
        } catch (Exception e)
        {
            System.err.println("Error parsing server message: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason)
    {
        this.session = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    public void setMessageHandler(Consumer<ServerMessage> handler)
    {
        this.messageHandler = handler;
    }

    public void sendConnect(String authToken, Integer gameID) throws Exception
    {
        UserGameCommand command = new UserGameCommand(CONNECT, authToken, gameID);
        sendCommand(command);
    }

    public void sendMakeMove(String authToken, Integer gameID, ChessMove move) throws Exception
    {
        UserGameCommand command = new UserGameCommand(MAKE_MOVE, authToken, gameID);
        command.setMove(move);
        sendCommand(command);
    }

    public void sendLeave(String authToken, Integer gameID) throws Exception
    {
        UserGameCommand command = new UserGameCommand(LEAVE, authToken, gameID);
        sendCommand(command);
    }

    public void sendResign(String authToken, Integer gameID) throws Exception
    {
        UserGameCommand command = new UserGameCommand(RESIGN, authToken, gameID);
        sendCommand(command);
    }

    private void sendCommand(UserGameCommand command) throws Exception
    {
        if (session == null || !session.isOpen())
        {
            throw new Exception("WebSocket is not connected");
        }
        String json = gson.toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }
}

