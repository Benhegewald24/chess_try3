import chess.*;
import server.Server;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        String user_input = scanner.nextLine();

        if (user_input.equalsIgnoreCase("help"))
        {
            help();
        }
        else
        {
            System.out.println("Invalid input");
            main(args);
        }
    }

    public static void help()
    {
        System.out.println("What would you like to do?");
        System.out.println("1. [L]og In");
        System.out.println("2. [R]egister");
        System.out.println("4. [Q]uit");
        Scanner scanner = new Scanner(System.in);
        String user_input = scanner.nextLine();
        if (user_input.equalsIgnoreCase("Log In") || user_input.equalsIgnoreCase("L"))
        {
            System.out.println("Username: ");
            String username = scanner.nextLine();
            System.out.println("Password: ");
            String password = scanner.nextLine();
            Server server = new Server();
            server.loginHandler(context);

            //Prompts the user to input login information. Calls the server login API to login the user.
            // When successfully logged in, the client should transition to the Postlogin UI.
        }
        if (user_input.equalsIgnoreCase("Register") || user_input.equalsIgnoreCase("R"))
        {
            System.out.println("Username: ");
            String username = scanner.nextLine();
            System.out.println("Password: ");
            String password = scanner.nextLine();
            System.out.println("Email: ");
            String email = scanner.nextLine();
            //. Calls the server register API to register and login the user. If successfully registered,
            // the client should be logged in and transition to the Postlogin UI.

            //registerHandler(context);
        }
        if (user_input.equalsIgnoreCase("Quit") || user_input.equalsIgnoreCase("Q"))
        {
            System.exit(0);
        }
        else
        {
            System.out.println("Invalid input");
            help();
        }
    }
}