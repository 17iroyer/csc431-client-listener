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
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
    
    final static private int MESSAGE_LENGTH = 10;
    
    private static String INPUT_FILE = "input.txt";
    private static String OUTPUT_FILE = "putni.txt";
    private static int port = 12000;
    private static String ipaddr = "127.0.0.1";    //The local address

    public static void main(String[] args) throws UnknownHostException, IOException
    {
        int charcount = 0;
        String msg = "";
  
        promptUser();

        File inputfile = new File(INPUT_FILE);
        File outputfile = new File(OUTPUT_FILE);
        Scanner filereader = new Scanner(inputfile);

        System.out.println("Started Client");

        //Take user input for what ip and port to use
        Socket s = new Socket(ipaddr, port); //127.0.0.1

        //Configure Scanner and buffered writer to read/write to server accordingly
        BufferedWriter servsend = new BufferedWriter(new PrintWriter(s.getOutputStream()));
        Scanner servscan = new Scanner(s.getInputStream());
        servscan.useDelimiter("");
        filereader.useDelimiter("");

        //Read characters until the end of the file.
        while(filereader.hasNext()) {
            msg += filereader.next();
            charcount++;

            //Every 10 characters, stop and send a message.
            //Wait for a response and write to ouput file
            if(charcount == 10) {
                servsend.write(msg);
                servsend.flush();
                charcount = 0;
                msg = "";
                String response = waitForResponse(servscan);
                writeToFile(outputfile, response);
            }
        }

        //If the last message has less than 10 characters, add spaces and send
        //Wait for response and write to output file
        if(charcount < MESSAGE_LENGTH) {
            msg = makeLastMsg(charcount, msg);            

            servsend.write(msg);
            servsend.flush();
            String response = waitForResponse(servscan);
            writeToFile(outputfile, response);
        }

        //Send the terminating message to the server
        servsend.write("\0\0\0\0\0\0\0\0\0\0");
        servsend.flush();

        //Close scanners
        s.close();
        filereader.close();
    }

    /**
     * writeToFile:
     * Appends a string to a given file.
     * @param file - file to append to
     * @param input -  string to write to append to file
     * @throws IOException
     */
    private static void writeToFile(File file, String input) throws IOException {
        FileWriter filewriter = new FileWriter(file, true);
        filewriter.append(input);
        filewriter.close();
    }

    /**
     * promptUser:
     * Prompts the user for an IPv4 address and a port to connect to
     * @throws IOException
     */
    private static void promptUser() throws IOException {
        Scanner userScan = new Scanner(System.in);
        
        System.out.print("Enter a .txt file to read from: ");
        INPUT_FILE = userScan.next();
        OUTPUT_FILE = reverseFile(INPUT_FILE);
        System.out.print("Enter an ip address to connect to: ");
        ipaddr = userScan.next();
        System.out.print("Enter a port to connect to: ");
        port = userScan.nextInt();
        userScan.close();
    }

    /**
     * makeLastMsg:
     * Adds any extra spaces to the last message.
     * Ensures last message has 10 characters
     * @param charcount - number of characters in msg
     * @param msg - partial message to change
     * @return
     */
    private static String makeLastMsg(int charcount, String msg) {
        String outmsg = msg;

        for(int i = charcount+1; i <= MESSAGE_LENGTH; i++) {
            outmsg = outmsg + " ";
        }

        return outmsg;
    }

    /**
     * waitForResponse:
     * Wait for the server response message, return it as a string
     * @param scan - scanner that is waiting for server response
     * @return response from server in a string
     */
    private static String waitForResponse(Scanner scan) {
        String response = "";

        for(int i = 0; i < MESSAGE_LENGTH; i++) {
            response += scan.next();
        }

        return response;
    }

    /**
     * reverseFile:
     * Takes a string an reverses its characters
     * @param s - string to flip
     * @return flipped string
     */
    private static String reverseFile(String s) {
        String output = "";
        int fileext = s.indexOf('.');
        char charArray[] = s.toCharArray();
        int i;
        for(i = fileext-1; i >=  0; i--) {
            output += charArray[i];
        }

        return output + ".txt";
    }
}