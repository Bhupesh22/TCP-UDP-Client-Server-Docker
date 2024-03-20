package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
    String performOperation(String input) throws RemoteException;
}
