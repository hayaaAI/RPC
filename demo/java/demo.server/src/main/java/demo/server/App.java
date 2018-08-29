package demo.server;

import hayaa.rpc.server.RpcServer;
import hayaa.serviceplatform.client.AppRoot;

public class App {
    public static void main(String[] args){
        try {
            AppRoot.StartApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RpcServer rpcServer=new RpcServer();
        try {
            rpcServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
