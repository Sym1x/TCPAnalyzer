import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        Socket clientSocket = serverSocket.accept(); // wait for a client
        System.out.println("Client connected: " + clientSocket.getInetAddress());

        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
        );

        String message = in.readLine(); // read one line
        System.out.println("Received: " + message);

        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
