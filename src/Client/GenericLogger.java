package Client;

/**
 * The GenericLogger interface defines methods for logging requests, responses,
 * and errors,
 * as well as generating timestamps.
 */
public interface GenericLogger {
    /**
     * Logs a request message.
     * 
     * @param log The request message to be logged.
     */
    void requestLog(String log);

    /**
     * Logs a response message.
     * 
     * @param log The response message to be logged.
     */
    void responseLog(String log);

    /**
     * Logs an error message.
     * 
     * @param log The error message to be logged.
     */
    void errorLog(String log);

    /**
     * Generates a timestamp.
     * 
     * @return A string representing the timestamp.
     */
    String timeStamp();
}
