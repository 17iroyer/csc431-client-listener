import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class girard_client {
    
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        System.out.println("Started Client");
        Socket s = new Socket("localhost", 12000); //127.0.0.1
        BufferedWriter bw = new BufferedWriter(new PrintWriter(s.getOutputStream()));
        Scanner scan = new Scanner(s.getInputStream());
        Scanner send = new Scanner(System.in);
        boolean connected = true;
        while(connected) {
            String msg = send.nextLine();
            bw.write(msg+"\n");
            bw.flush();
            String response = scan.next();
            System.out.println(response);
            connected = false;
        }
        s.close();
        send.close();
    }
}
