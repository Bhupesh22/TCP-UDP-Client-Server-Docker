package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;
import java.util.Scanner;

import Server.RemoteInterface;

public class RPCClientImpl extends AbstractClient {

    public RPCClientImpl() {

    }

    public void startClient() throws IOException {
        prepopulateRPC();
        scanner = new Scanner(System.in);
        while (true) {
            System.out.println("---------------------------------------");
            System.out.print(
                    "Please choose your operations: \n1. PUT\n2. GET\n3. DELETE\n4. GET-ALL\n5. DELETE-ALL\nChoose operation number: ");
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
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
            processRequest(request);
            

        }

    }
    private void prepopulateRPC() {
        sendAndProcessRequests("PUT", 1, 10);
        sendAndProcessRequests("GET", 1, 5);
        sendAndProcessRequests("DELETE", 1, 5);
    }

    private void sendAndProcessRequests(String requestType, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (requestType == "PUT") {
                request = requestType + " " + i + " " + (i * 100);
            } else {
                request = requestType + " " + i;
            }
            processRequest(request);
        }
    }

    private void processRequest(String request) {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(null);

            RemoteInterface stub = (RemoteInterface) registry.lookup("KeyStore");
            requestLog(request);
            String result = stub.performOperation(request);
            responseLog(result);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
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
}
