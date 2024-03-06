package Server;

import java.io.IOException;

/**
 * The StartTCPServer class is responsible for starting the TCP server.
 */
public class StartTCPServer {
    /**
     * Main method to start the TCP server.
     * 
     * @param args The command-line arguments. Expects a single argument
     *             representing the port number.
     * @throws IllegalArgumentException If the input provided is invalid.
     * @throws IOException              If an I/O error occurs while starting the
     *                                  server.
     */
    public static void main(String[] args) throws IllegalArgumentException, IOException {

        TCPServerImpl tcpServerImpl = new TCPServerImpl();

        System.out.println("TCP Server about to start ....");
        if (args.length == 0) {
            throw new IllegalArgumentException("No input provided! Please provide a port number.");
        }
        int tcpPort = -1;
        try {
            tcpPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input! Please provide a valid number for the port.");
        }

        if (tcpPort > 65535 || tcpPort < 0) {
            throw new IllegalArgumentException("Invalid input! Please provide a valid port number (0 - 65535).");
        }
        System.out.println("TCP Server started on port -> " + tcpPort);
        try {
            tcpServerImpl.startServer(tcpPort);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
