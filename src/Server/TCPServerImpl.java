package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class TCPServerImpl extends AbstractServer implements Server {

    public TCPServerImpl() throws IOException {
        properties = new Properties();
        properties.clear();
        write = new FileOutputStream("Server/map.properties");
        properties.store(write, null);
    }

    @Override
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Timestamp : " + timeStamp());

            Socket clientSocket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String input = dataInputStream.readUTF();
                requestLog(input, String.valueOf(clientSocket.getInetAddress()),
                        String.valueOf(clientSocket.getPort()));
                String parsedInput = input.trim().replaceAll("\\s{2,}", " ");
                String result = performOperation(parsedInput.split(" "));
                responseLog(result);
                dataOutputStream.writeUTF(result);
                dataOutputStream.flush();
            }

        } catch (Exception e) {
            System.out.println(timeStamp() + " Error: " + e);
        }
    }
}
