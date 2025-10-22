package service;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import results.ListGamesResult;
import results.CreateGameResult;
import results.JoinGameResult;
import model.AuthData;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;


public class GameService
{
    private final DataAccess dataAccess;
    
    public GameService(DataAccess dataAccess) 
    {
        this.dataAccess = dataAccess;
    }
    
    public ListGamesResult listGames(String authToken) throws DataAccessException 
    {
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null) 
        {
            throw new DataAccessException("Error: unauthorized");
        }
        
        return new ListGamesResult(dataAccess.listGames());
    }
    
  
    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws DataAccessException 
    {
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null) 
        {
            throw new DataAccessException("Error: unauthorized");
        }
        
        if (request.gameName() == null || request.gameName().isEmpty()) 
        {
            throw new DataAccessException("Error: bad request");
        }
        
        int gameId = dataAccess.createGame(request.gameName());
        
        return new CreateGameResult(gameId);
    }
    

    public JoinGameResult joinGame(JoinGameRequest request, String authToken) throws DataAccessException 
    {
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null) 
        {
            throw new DataAccessException("Error: unauthorized");
        }
        
        if (request.playerColor() == null) 
        {
            throw new DataAccessException("Error: bad request");
        }
        
        GameData game = dataAccess.getGame(request.gameID());

        if (game == null)
        {
            throw new DataAccessException("Error: bad request");
        }
        
        String playerColor = request.playerColor();

        if (playerColor.equals("WHITE"))
        {
            if (game.whiteUsername() != null) 
            {
                throw new DataAccessException("Error: already taken");
            }

            GameData updatedGame = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
            dataAccess.updateGame(updatedGame);
        } 

        else if (playerColor.equals("BLACK"))
        {
            if (game.blackUsername() != null) 
            {
                throw new DataAccessException("Error: already taken");
            }

            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(),game.game());
            dataAccess.updateGame(updatedGame);
        } 
        
        else 
        {
            throw new DataAccessException("Error: bad request");
        }
        
        return new JoinGameResult();
    }
}
