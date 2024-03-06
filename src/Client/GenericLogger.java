package Client;

public interface GenericLogger {
    void requestLog(String log);

    void responseLog(String log);

    void errorLog(String log);

    String timeStamp();
}
