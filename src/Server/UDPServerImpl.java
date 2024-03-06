package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * The UDPServerImpl class represents an implementation of a UDP server.
 */
public class UDPServerImpl extends AbstractServer implements Server {

    /**
     * Constructor for UDPServerImpl.
     * Initializes the properties file for storing key-value pairs.
     * 
     * @throws IOException If an I/O error occurs while initializing the properties
     *                     file.
     */
    public UDPServerImpl() throws IOException {
        this.properties = new Properties();
        this.properties.clear();
        this.write = new FileOutputStream("Server/map.properties");
        this.properties.store(write, null);
    }

    /**
     * Starts the UDP server on the specified port.
     * 
     * @param port The port number on which the server will listen for incoming
     *             datagrams.
     */
    @Override
    public void startServer(int port) {
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            System.out.println("Timestamp : " + timeStamp());

            byte[] requestBuffer = new byte[512];
            byte[] responseBuffer;

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(requestBuffer, requestBuffer.length);
                datagramSocket.receive(receivePacket);
                InetAddress client = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                byte[] payload = extractPayload(receivePacket);
                byte[] checksum = extractChecksum(receivePacket);
                long clientChecksum = bytesToLong(checksum);
                long serverChecksum = generateChecksum(payload);
                if (serverChecksum != clientChecksum) {
                    errorLog("Malformed data packet!");
                    continue;
                }
                String request = new String(payload, receivePacket.getOffset(), payload.length);
                requestLog(request, client.toString(), String.valueOf(clientPort));
                if (payload.length > 512) {
                    errorLog("Received packet exceeds maximum allowed size.");
                    continue;
                }

                try {
                    String parsedInput = request.trim().replaceAll("\\s{2,}", " ");
                    String result = performOperation(parsedInput.split(" "));
                    responseLog(result);
                    responseBuffer = result.getBytes();

                } catch (IllegalArgumentException e) {
                    String errorMessage = e.getMessage();
                    errorLog(errorMessage);
                    responseBuffer = errorMessage.getBytes();
                }

                try {
                    DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length,
                            client, clientPort);
                    datagramSocket.send(responsePacket);
                    requestBuffer = new byte[512];
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLog("Error! Please make sure IP and Port are valid and try again.");
        }
    }

    private static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();// need flip
        return buffer.getLong();
    }

    private static byte[] extractChecksum(DatagramPacket packet) {

        byte[] data = packet.getData();
        byte[] cs = new byte[8];

        for (int i = 0; i < 8; i++) {
            cs[i] = data[i];
        }

        return cs;
    }

    private static byte[] extractPayload(DatagramPacket packet) {
        int length = packet.getLength();
        byte[] data = packet.getData();
        byte[] payload = new byte[length - 8];

        int j = 0;

        for (int i = 8; i < length; i++) {
            payload[j] = data[i];
            j++;
        }

        return payload;
    }

    private static long generateChecksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

}