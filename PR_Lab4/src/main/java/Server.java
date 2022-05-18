import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 5000;

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening to port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client has connected: " + clientSocket);

                Thread thread = new ClientHandler(clientSocket);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
