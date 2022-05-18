import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;


    public ClientHandler(Socket clientSocket) {
        this.socket = clientSocket;
    }

    @Override
    public void run() {

        String received;

        while (true) {
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                dataOutputStream.writeUTF("Enter the name of the file or type exit to close the connection");
                received = dataInputStream.readUTF();

                if (received.equalsIgnoreCase("exit")) {
                    System.out.println("Client " + this.socket + "has closed the connection.");
                    this.socket.close();
                    break;
                } else {
                    System.out.println("Downloading file " + received);
                    String destination = "./downloads/" + received;
                    receiveFile(destination);
                    dataOutputStream.writeUTF("File " + received + " has been uploaded successfully!");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = dataInputStream.readLong();     // read file size
        System.out.println("File size: " + size);
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;      // read upto file size
        }
        System.out.println("File received");
        fileOutputStream.close();
    }
}
