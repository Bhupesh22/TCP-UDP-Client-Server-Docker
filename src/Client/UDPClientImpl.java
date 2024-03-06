package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class UDPClientImpl extends AbstractClient{

    public UDPClientImpl(InetAddress serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    public void startClient() throws IOException {
        scanner = new Scanner(System.in);

        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            
            datagramSocket.setSoTimeout(10000);
            System.out.println(getTimeStamp() + " Client started");

            prepopulateRequests(serverIP, port);

            while (true) {
                
                System.out.println("---------------------------------------");
                System.out.print("Please choose your operations: \n1. PUT\n2. GET\n3. DELETE\n4. GET-ALL\n5. DELETE-ALL\nChoose operation number: ");

                String operation = scanner.nextLine().trim();
                
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
                    System.out.println("Please choose a valid operation!");
                    continue;
                }

                requestLog(request);
                sendRequest(datagramSocket, serverIP, port, request);
            }
        } catch (UnknownHostException | SocketException e) {
            System.out.println(
                    "Host or Port unknown error, try again!");
        }
    }

    private static void getKey() throws IOException {
        System.out.print("Enter key: ");
        key = scanner.nextLine();
    }

    private static void getValue() throws IOException {
        System.out.print("Enter Value: ");
        value = scanner.nextLine();
    }

    private static void prepopulateRequests(InetAddress serverIP, int port) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        System.out.println("Prepopulating UDP");
        sendRequests("PUT", 1, 10, port, datagramSocket, serverIP);
        sendRequests("GET", 1, 5, port, datagramSocket, serverIP);
        sendRequests("DELETE", 1, 5, port, datagramSocket, serverIP);
    }

    private static void sendRequests(String requestType, int start, int end, int port, DatagramSocket datagramSocket,
            InetAddress serverIP) throws IOException {
        for (int i = start; i < end + 1; i++) {
            String request = "";
            if (requestType == "PUT") {
                request = requestType + " " + i + " " + (i * 100);
            } else {
                request = requestType + " " + i;
            }
            sendRequest(datagramSocket, serverIP, port, request);
        }
    }

    private static void sendRequest(DatagramSocket datagramSocket, InetAddress serverIP, int port, String request)
            throws IOException {
        byte[] requestBuffer = request.getBytes();
        if (requestBuffer.length > 65499) {
            System.out.println("Error: Request size exceeds the maximum allowed limit.");
            return;
        }

        long checksum = generateChecksum(requestBuffer);

        byte[] requestData = generateData(requestBuffer, checksum);
        DatagramPacket requestPacket = createDatagramPacket(requestData, serverIP, port);
        datagramSocket.send(requestPacket);

        try {
            byte[] resultBuffer = receiveResponse(datagramSocket);
            String result = new String(resultBuffer);
            responseLog(result);
        } catch (java.net.SocketTimeoutException e) {
            System.out.println("Timeout occurred. The server did not respond within the specified time.");
        }
    }

    private static long generateChecksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    private static byte[] generateData(byte[] request, long checksum) {
        byte[] data = new byte[request.length + 8];
        byte[] cs = ByteBuffer.allocate(8).putLong(checksum).array();

        int i = 0;

        for (byte b : cs) {
            data[i] = b;
            i++;
        }

        for (byte b : request) {
            data[i] = b;
            i++;
        }

        return data;
    }

    private static DatagramPacket createDatagramPacket(byte[] buffer, InetAddress serverIP, int port) {
        return new DatagramPacket(buffer, buffer.length, serverIP, port);
    }

    private static byte[] receiveResponse(DatagramSocket datagramSocket) throws IOException {
        byte[] resultBuffer = new byte[512];
        DatagramPacket resultPacket = new DatagramPacket(resultBuffer, resultBuffer.length);
        datagramSocket.receive(resultPacket);
        return resultPacket.getData();
    }
}
