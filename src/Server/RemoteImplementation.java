package Server;

import java.io.IOException;

public class RemoteImplementation implements RemoteInterface {
    private RPCServerImpl rpc;

    public RemoteImplementation() throws IOException{
        this.rpc = new RPCServerImpl();
    }
    @Override
    public String performOperation(String input) {
        return this.rpc.performOperation(input.trim().replaceAll("\\s{2,}", " ").split(" "));
    }
}
