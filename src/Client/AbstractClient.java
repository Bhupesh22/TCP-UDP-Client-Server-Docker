package Client;

import java.io.BufferedReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * The AbstractClient class represents a base class for client applications.
 * It contains common functionality and utilities that can be used by
 * subclasses.
 */
public class AbstractClient {

    /** The IP address of the server. */
    protected InetAddress serverIP;

    /** The port number for communication with the server. */
    protected int port;

    /** The key, value, request used for requests to the server. */
    protected static String key, value, request;

    /** The scanner object for user input. */
    protected static Scanner scanner;

    /** The buffered reader for reading server responses. */
    protected static BufferedReader bufferedReader;

    /**
     * Logs a request message along with a timestamp.
     * 
     * @param str The request message.
     */
    public static void requestLog(String str) {
        System.out.println(getTimeStamp() +
                " [Request] -> " + str);
    }

    /**
     * Logs a response message along with a timestamp.
     * 
     * @param str The response message.
     */
    public static void responseLog(String str) {
        System.out.println(getTimeStamp() +
                " [Response] -> " + str + "\n");
    }

    /**
     * Generates a timestamp in the format "MM-dd-yyyy HH:mm:ss.SSS".
     * 
     * @return A string representing the timestamp.
     */
    public static String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        return "[Time: " + simpleDateFormat.format(new Date()) + "]";
    }
}
