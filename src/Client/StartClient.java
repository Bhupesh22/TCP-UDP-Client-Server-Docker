package Client;

import java.io.IOException;
import java.net.InetAddress;

public class StartClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 3 || Integer.parseInt(args[1]) > 65535) {
            throw new IllegalArgumentException(
                    "Invalid argument! Please provide a valid IP address, port number (0-65535), and protocol (TCP or UDP) and start again");
        }

        String ipAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String protocol = args[2].toLowerCase();

        InetAddress serverIP = InetAddress.getByName(ipAddress);

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
