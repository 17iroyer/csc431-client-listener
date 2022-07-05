/**
 * Ian Royer
 * 3-10-2021
 * CSC341 Project 1
 * Server/Client Communication
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;

public class Server_Thread implements Runnable{

    private Socket connectedsocket;

    public Server_Thread(Socket s) {
        connectedsocket = s;
    }

    @Override
    public void run() {
        int passes = 0;
        Socket s = connectedsocket;
        Scanner clientIn = null;
        BufferedWriter bw = null;
        
        //Set up the input scanner and output writer
        try {
            clientIn = new Scanner(s.getInputStream());
            bw = new BufferedWriter(new PrintWriter(s.getOutputStream()));
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        clientIn.useDelimiter("");
        
        //Print to the log that a client has connected
        System.out.println("Client Connected");
        try {
            printLog(s, LocalTime.now(), "Connected", passes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read until the terminating message is found
        while(true){
            String msg = "";
            for(int i = 0; i < 10; i++) {
                msg += clientIn.next();
            }

            //Check if the terminating message is sent
            if(msg.equals("\0\0\0\0\0\0\0\0\0\0")) {
                break;
            }

            //Write the reverse string back and increase number of passes
            try {
                bw.write(reverseString(msg));
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            passes++;
        }

        //Close the client and write to the log file 
        try {
            s.close();
            clientIn.close();
            printLog(s, LocalTime.now(), "Disconnected", passes);
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Client Disconnected");        
    }
    


    /**
     * reverseString:
     * Takes a string an reverses its characters
     * @param s - string to flip
     * @return flipped string
     */
    private static String reverseString(String s) {
        String output = "";
        char charArray[] = s.toCharArray();
        int i;
        for(i = s.length()-1; i >=  0; i--) {
            output += charArray[i];
        }

        return output;
    }

    /**
     * printLog:
     * Append a message to the log.txt file upon connect/disconnect
     * @param s - socket to get ip address from
     * @param time - current time 
     * @param status - string telling if there was a connect or disconnect
     * @param passes - number of input passes made to read full message
     * @throws IOException
     */
    private static void printLog(Socket s, LocalTime time, String status, int passes) throws IOException {
        FileWriter writer = new FileWriter(new File("log.txt"), true);

        writer.append(s.getInetAddress() + " | " + time + " | " +  status + " with " + (passes*10) + " characters processed\n");
        writer.close();
    }
}
