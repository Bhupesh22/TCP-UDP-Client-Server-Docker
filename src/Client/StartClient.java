package Client;

import java.io.IOException;
import java.net.InetAddress;

/**
 * The StartClient class represents the entry point for the client application.
 * It parses command-line arguments, validates them, and starts the appropriate
 * client based on the specified protocol.
 */
public class StartClient {
    /**
     * The main method of the StartClient class.
     * 
     * @param args Command-line arguments: IP address, port number, and protocol
     *             (TCP or UDP).
     * @throws IOException              If an I/O error occurs.
     * @throws IllegalArgumentException If the command-line arguments are invalid.
     */
    public static void main(String[] args) throws IOException {
        // Validate command-line arguments
        if (args.length != 3 || Integer.parseInt(args[1]) > 65535) {
            throw new IllegalArgumentException(
                    "Invalid argument! Please provide a valid IP address, port number (0-65535), and protocol (TCP or UDP) and start again");
        }

        // Extract command-line arguments
        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String protocol = args[2].toLowerCase();

        // Resolve server IP address
        InetAddress serverIP = InetAddress.getByName(ipAddress);

        // Start client based on the specified protocol
        switch (protocol) {
            case "udp":
                UDPClientImpl udp = new UDPClientImpl(serverIP, port);
                udp.startClient();
                break;
            case "tcp":
                TCPClientImpl tcp = new TCPClientImpl(serverIP, port);
                tcp.startClient();
                break;
            default:
                throw new IllegalArgumentException(
                        "Invalid argument! Please provide a valid protocol (TCP or UDP) and start again");
        }
    }
}
