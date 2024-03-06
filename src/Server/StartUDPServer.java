package Server;

import java.io.IOException;

/**
 * The StartUDPServer class is responsible for starting the UDP server.
 */
public class StartUDPServer {
    /**
     * Main method to start the UDP server.
     * 
     * @param args The command-line arguments. Expects a single argument
     *             representing the port number.
     * @throws IllegalArgumentException If the input provided is invalid.
     * @throws IOException              If an I/O error occurs while starting the
     *                                  server.
     */
    public static void main(String[] args) throws IllegalArgumentException, IOException {
        UDPServerImpl udpServerImpl = new UDPServerImpl();
        System.out.println("UDP Server about to start ....");
        if (args.length == 0) {
            throw new IllegalArgumentException("No input provided! Please provide a port number.");
        }
        int udpPort = -1;
        try {
            udpPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input! Please provide a valid number for the port.");
        }
        System.out.println("UDP Server started on port -> " + udpPort);
        try {
            udpServerImpl.startServer(udpPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
