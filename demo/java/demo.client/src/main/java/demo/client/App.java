package demo.client;

import demo.interfaceservice.IService;
import demo.interfaceservice.Paramater;
import demo.interfaceservice.RpcData;
import hayaa.rpc.client.RpcClient;
import hayaa.rpc.client.ServiceFactory;
import hayaa.serviceplatform.client.AppRoot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {
        try {
            AppRoot.StartApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RpcClient rpcClient=new RpcClient();
        rpcClient.run();
        IService service=ServiceFactory.createService("demo.interfaceservice.IService");
        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = null;
            System.out.println("Enter your value:");
            str = br.readLine();
            Paramater rpcParamater = new Paramater();
            rpcParamater.setTitle(str);
            rpcParamater.setVal(str.length());
            RpcData rpcData = service.DoRpc(rpcParamater);
            System.out.println("content:"+rpcData.getTitle()+";val:"+rpcData.getVal());
        }
    }
}
