/**
 * Ian Royer
 * 3-10-2021
 * CSC341 Project 1
 * Server/Client Communication
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    
    private static int port;

    public static void main(String[] args) throws IOException
    {
        
        promptUser();        
        ServerSocket ss = new ServerSocket(port);  //TCP by default

        //If a client connects, create a new thread
        while(true) {
            System.out.println("Listening for connection...");
            (new Thread(new Server_Thread(ss.accept()))).start();
        }
    }   

    
    /**
     * promptUser:
     * prompts the user for a port to listen to
     * @throws IOException
     */
    private static void promptUser() throws IOException {
        Scanner portSelector = new Scanner(System.in);

        System.out.print("Enter a port to listen to: ");
        port = portSelector.nextInt();

        portSelector.close();
    }
}