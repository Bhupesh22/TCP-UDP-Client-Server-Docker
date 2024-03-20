package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import Server.AbstractServer;

public class RPCServerImpl extends AbstractServer {
    public RPCServerImpl() throws IOException{
        properties = new Properties();
        properties.clear();
        write = new FileOutputStream("Server/map.properties");
        properties.store(write, null);
    }
}
