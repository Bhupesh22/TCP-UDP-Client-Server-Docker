package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

/**
 * The AbstractServer class provides common functionalities and utilities for
 * server implementations.
 */
public abstract class AbstractServer {

    protected OutputStream write;
    protected Properties properties;

    /**
     * Logs the incoming request from a client.
     * 
     * @param log       The request log.
     * @param ipAddress The IP address of the client.
     * @param port      The port number of the client.
     */
    public void requestLog(String log, String ipAddress, String port) {
        System.out.println(timeStamp() + " [REQUEST-LOG] Request from IP : " + ipAddress + ", Port : " + port
                + " -> Request :" + log);
    }

    /**
     * Logs the server response.
     * 
     * @param log The response log.
     */
    public void responseLog(String log) {
        System.out.println(timeStamp() + " [RESPONSE-LOG] Response -> " + log);
    }

    /**
     * Logs errors encountered during server operation.
     * 
     * @param log The error log.
     */
    public void errorLog(String log) {
        System.out.println(timeStamp() + "[ERROR-LOG] Error -> " + log);
    }

    /**
     * Retrieves the current timestamp.
     * 
     * @return The formatted timestamp.
     */
    public String timeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        return "<Time: " + simpleDateFormat.format(new Date()) + ">";
    }

    /**
     * Performs the requested operation based on the input provided.
     * Supports operations such as PUT, GET, DELETE, GET-ALL, and DELETE-ALL.
     * 
     * @param input An array containing the input data.
     * @return A string representing the result of the operation.
     */
    public String performOperation(String[] input) {
        try {

            String operation = getOperation(input);
            String key = getKey(input);
            String value = getValue(input);
            switch (operation) {
                case "PUT": {
                    String result = performPut(key, value);
                    responseLog(result);
                    return result;
                }
                case "DELETE": {
                    String result = performDelete(key);
                    responseLog(result);
                    return result;
                }
                case "GET": {
                    String result = performGet(key);
                    responseLog(result);
                    return result;
                }
                case "GET-ALL": {
                    String result = performGetAll();
                    responseLog(result);
                    return result;
                }
                case "DELETE-ALL": {
                    String result = performDeleteAll();
                    responseLog(result);
                    return result;
                }
                default:
                    throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return "BAD REQUEST!:  Please view README.md to check available operations." + e;
        }
    }

    /**
     * Extracts the operation from the input array and returns it in uppercase.
     * 
     * @param input An array containing the input data.
     * @return The operation extracted from the input.
     */
    protected String getOperation(String[] input) {
        return input[0].toUpperCase();
    }

    /**
     * Extracts the key from the input array.
     * 
     * @param input An array containing the input data.
     * @return The key extracted from the input.
     */
    protected String getKey(String[] input) {
        String key = "";
        if (input.length > 1) {
            key = input[1];
        }
        return key;
    }

    /**
     * Extracts the value from the input array.
     * 
     * @param input An array containing the input data.
     * @return The value extracted from the input.
     */
    protected String getValue(String[] input) {
        String value = "";
        if (input.length == 3) {
            value = input[2];
        }
        return value;
    }

    /**
     * Retrieves the value associated with the given key from the properties.
     * 
     * @param key The key to search for.
     * @return A string representing the result of the operation.
     */
    protected String performGet(String key) {
        String value = properties.getProperty(key);
        String result = value == null ? "No value found for key \"" + key + "\""
                : "Key: \"" + key + "\" ,Value: \"" + value + "\"";
        return result;
    }

    /**
     * Inserts a new key-value pair into the properties and stores them.
     * 
     * @param key   The key to insert.
     * @param value The value to insert.
     * @return A string representing the result of the operation.
     * @throws IOException If an I/O error occurs.
     */
    protected String performPut(String key, String value) throws IOException {
        properties.setProperty(key, value);
        properties.store(this.write, null);
        String result = "Inserted key \"" + key + "\" with value \"" + value + "\"";
        return result;
    }

    /**
     * Deletes the key-value pair associated with the given key from the properties
     * and stores the changes.
     * 
     * @param key The key to delete.
     * @return A string representing the result of the operation.
     * @throws IOException If an I/O error occurs.
     */
    protected String performDelete(String key) throws IOException {
        String result = "";
        if (properties.containsKey(key)) {
            properties.remove(key);
            properties.store(this.write, null);
            result = "Deleted key \"" + key + "\"" + " successfully!";
        } else {
            result = "Key not found.";
        }
        return result;
    }

    /**
     * Deletes all key-value pairs from the properties and stores the changes.
     * 
     * @return A string representing the result of the operation.
     * @throws IOException If an I/O error occurs.
     */
    protected String performDeleteAll() throws IOException {
        Set<Object> keys = properties.keySet();
        for (Object x : keys) {
            performDelete((String) x);
        }
        return properties.keySet().size() == 0 ? "Delete all operation successful"
                : "Delete all operation unsuccessful";
    }

    /**
     * Retrieves all key-value pairs from the properties.
     * 
     * @return A string representing all key-value pairs.
     */
    protected String performGetAll() {
        Set<Object> keys = properties.keySet();
        if (keys.size() == 0) {
            return "Key-Value Store is empty";
        }
        String result = "\n";
        for (Object x : keys) {
            result += performGet((String) x) + "\n";
        }
        return result;
    }
}
