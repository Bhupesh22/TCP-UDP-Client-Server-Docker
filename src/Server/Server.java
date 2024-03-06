package Server;

/**
 * The Server interface defines the contract for starting a server on a
 * specified port.
 */
interface Server {
    /**
     * Starts the server on the specified port.
     * 
     * @param port The port number on which the server will listen for incoming
     *             connections.
     */
    void startServer(int port);
}