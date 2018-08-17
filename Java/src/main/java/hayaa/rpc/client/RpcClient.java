package hayaa.rpc.client;

/**
 * rpc客户端主类
 */
public class RpcClient {
    /**
     * rpc客户端启动入口函数
     */
    public synchronized void run(){
        ClientHelper.get_instance().init();
    }
}
