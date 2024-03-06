package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public abstract class AbstractServer{

    protected OutputStream write;
    protected Properties properties;

    public void requestLog(String log, String ipAddress, String port) {
        System.out.println(timeStamp() + " [REQUEST-LOG] Request from IP : " + ipAddress + ", Port : " + port
                + " -> Request :" + log);
    }

    public void responseLog(String log) {
        System.out.println(timeStamp() + " [RESPONSE-LOG] Response -> " + log);
    }


    public void errorLog(String log) {
        System.out.println(timeStamp() + "[ERROR-LOG] Error -> " + log);
    }

    public String timeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        return "<Time: " + simpleDateFormat.format(new Date()) + ">";
    }
    
    public String performOperation(String[] input) {
        try {

            String operation = getOperation(input);
            String key = getKey(input);
            String value = getValue(input);
            switch (operation) {
                case "PUT": {
                    return performPut(key, value);
                }
                case "DELETE": {
                    return performDelete(key);
                }
                case "GET": {
                    return performGet(key);
                }
                case "GET-ALL": {
                    return performGetAll();
                }
                case "DELETE-ALL": {
                    return performDeleteAll();
                }
                default:
                    throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return "BAD REQUEST!:  Please view README.md to check available operations." + e;
        }
    }

    protected String getOperation(String[] input) {
        return input[0].toUpperCase();
    }

    protected String getKey(String[] input) {
        String key = "";
        if (input.length > 1) {
            key = input[1];
        }
        return key;
    }

    protected String getValue(String[] input) {
        String value = "";
        if (input.length == 3) {
            value = input[2];
        }
        return value;
    }

    protected String performGet(String key) {
        String value = properties.getProperty(key);
        String result = value == null ? "No value found for key \"" + key + "\""
                : "Key: \"" + key + "\" ,Value: \"" + value + "\"";
        return result;
    }

    protected String performPut(String key, String value) throws IOException {
        properties.setProperty(key, value);
        properties.store(this.write, null);
        String result = "Inserted key \"" + key + "\" with value \"" + value + "\"";
        return result;
    }

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

    protected String performDeleteAll() throws IOException {
        Set<Object> keys = properties.keySet();
        for (Object x : keys) {
            performDelete((String) x);
        }
        return properties.keySet().size() == 0 ? "Delete all operation successful"
                : "Delete all operation unsuccessful";
    }

    protected String performGetAll() {
        Set<Object> keys = properties.keySet();
        if(keys.size() == 0){
            return "Key-Value Store is empty";
        }
        String result = "\n";
        for (Object x : keys) {
            result += performGet((String) x) + "\n";
        }
        return result;
    }
}
