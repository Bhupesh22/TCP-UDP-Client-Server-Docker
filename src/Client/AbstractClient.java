package Client;

import java.io.BufferedReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AbstractClient {

    protected InetAddress serverIP;
    protected int port;
    protected static String key, value, request;
    protected static Scanner scanner;
    protected static BufferedReader bufferedReader;

    public static void requestLog(String str) {
        System.out.println(getTimeStamp() +
                " [Request] -> " + str);
    }

    public static void responseLog(String str) {
        System.out.println(getTimeStamp() +
                " [Response] -> " + str + "\n");
    }

    public static String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        return "[Time: " + simpleDateFormat.format(new Date()) + "]";
    }
}
