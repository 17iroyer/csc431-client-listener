import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class girard_server 
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(12000);  //TCP by default

        //usually it's own thread
        System.out.println("Listening...");
        Socket s = ss.accept();
        Scanner scan = new Scanner(s.getInputStream());
        BufferedWriter bw = new BufferedWriter(new PrintWriter(s.getOutputStream()));
        boolean connected = true;

        while(connected) {
            String msg = scan.nextLine();
            System.out.println(msg);
            bw.write("Message_Recieved");
            bw.flush();
            connected = false;
        }
        s.close();
        ss.close();
    }
}
