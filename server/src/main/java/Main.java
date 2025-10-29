import chess.*;
import dataaccess.MySqlDataAccess;
import server.Server;
import service.UserService;

public class Main
{
    public static void main(String[] args)
    {
        Server server = new Server();
        server.run(8080);

        //^^^ Here we specify whether we want to use local or SQL database to store data
        System.out.println("♕ 240 Chess Server");
    }
}