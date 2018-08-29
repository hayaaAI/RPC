package demo.server;

import demo.interfaceservice.IService;
import demo.interfaceservice.Paramater;
import demo.interfaceservice.RpcData;

public class ServiceProvider implements IService {
    @Override
    public RpcData DoRpc(Paramater paramater) {
        System.out.println("客户端数据:"+paramater.getTitle()+";长度:"+paramater.getVal());
        RpcData rpcData=new RpcData();
        rpcData.setTitle("服务端接收:" +paramater.getTitle());
        rpcData.setVal(paramater.getVal()+ 10);
        return rpcData;
    }
}
