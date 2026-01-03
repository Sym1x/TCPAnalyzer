import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        String serverIP = "localhost";
        int port = 5000;

        Socket socket = new Socket(serverIP, port);
        System.out.println("Connected to server");

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Gium ex Ciwcbis. Eo oyb k ksbi by jezx");

        out.close();
        socket.close();
    }
}
