package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;

public class TCPClientImpl extends AbstractClient{


    public TCPClientImpl(InetAddress serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    public void startClient() {
        scanner = new Scanner(System.in);
        try (Socket s = new Socket()) {
            
            s.connect(new InetSocketAddress(serverIP, port), 10000);
            
            DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(getTimeStamp() + " Client started on port: " + s.getPort());

            prepopulateTCPRequests(dataOutputStream, dataInputStream);

            while (true) {
                
                System.out.println("---------------------------------------");
                System.out.print("Please choose your operations: \n1. PUT\n2. GET\n3. DELETE\n4. GET-ALL\n5. DELETE-ALL\nChoose operation number: ");

                String operation = bufferedReader.readLine().trim();
                if (Objects.equals(operation, "1")) {
                    getKey();
                    getValue();
                    request = "PUT " + key + " " + value;
                } else if (Objects.equals(operation, "2")) {
                    getKey();
                    request = "GET " + key;
                } else if (Objects.equals(operation, "3")) {
                    getKey();
                    request = "DELETE " + key;
                } else if (Objects.equals(operation, "4")) {
                    request = "GET-ALL";
                } else if (Objects.equals(operation, "5")) {
                    request = "DELETE-ALL";
                } else {
                    System.out.println("Please choose a valid operation.");
                    continue;
                }
                sendPacket(dataOutputStream, request);
                String response = receivePacket(dataInputStream);
                if (response.startsWith("ERROR")) {
                    System.out.println("Received error response from the server: " + response);
                } else {
                    responseLog(response);
                }
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println("Host or Port unknown, please provide a valid hostname and port number.");
        } catch (SocketTimeoutException e) {
            System.out.println("Connection timed out. Please check the server availability and try again.");
        } catch (Exception e) {
            System.out.println("Exception occurred!" + e);
        }
    }

    private static void prepopulateTCPRequests(DataOutputStream dataOutputStream, DataInputStream dataInputStream)
            throws IOException {
        System.out.println("Prepopulating TCP");

        sendAndProcessRequests(dataOutputStream, dataInputStream, "PUT", 1, 10);
        sendAndProcessRequests(dataOutputStream, dataInputStream, "GET", 1, 5);
        sendAndProcessRequests(dataOutputStream, dataInputStream, "DELETE", 1, 5);
    }

    private static void sendAndProcessRequests(DataOutputStream dataOutputStream, DataInputStream dataInputStream,
            String requestType, int start, int end) throws IOException {
        for (int i = start; i <= end; i++) {
            if (requestType == "PUT") {
                request = requestType + " " + i + " " + (i * 100);
            } else {
                request = requestType + " " + i;
            }
            String response = sendAndReceivePacket(dataOutputStream, dataInputStream, request);

            if (response.startsWith("ERROR")) {
                System.out.println("Received error response from the server: " + response);
            } else {
                responseLog(response);
            }
        }
    }

    private static String sendAndReceivePacket(DataOutputStream dataOutputStream, DataInputStream dataInputStream,
            String request) throws IOException {
        sendPacket(dataOutputStream, request);
        return receivePacket(dataInputStream);
    }

    private static void getKey() throws IOException {
        System.out.print("Enter key: ");
        key = scanner.nextLine();
    }

    private static void getValue() throws IOException {
        System.out.print("Enter Value: ");
        value = scanner.nextLine();
    }

    
    private static void sendPacket(DataOutputStream outputStream, String packet) throws IOException {
        outputStream.writeUTF(packet);
        outputStream.flush();
        requestLog(packet);
    }

    private static String receivePacket(DataInputStream inputStream) throws IOException {
        return inputStream.readUTF();
    }

}
