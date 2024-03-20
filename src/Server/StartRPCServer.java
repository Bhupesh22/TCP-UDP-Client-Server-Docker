package Server;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Server.RemoteInterface;

public class StartRPCServer extends RemoteImplementation{
    public StartRPCServer() throws IOException {}
    public static void main(String[] args) {
        try {
            RemoteImplementation obj = new RemoteImplementation();
            RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("KeyStore", stub);
            System.err.println("RPC Server Ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}