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

        var service = new UserService(new MySqlDataAccess());
        var server = new Server(service).run(port);
        port = server.port();

        //^^^ Here we specify whether we want to use local or SQL database to store data
        System.out.println("♕ 240 Chess Server");
    }
}