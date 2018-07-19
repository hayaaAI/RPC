package hayaa.rpc;
public class AppComponentRoot {
    private IRPCProviderService g_providerService;

    /**
     * 构建服务提供者
     * @param providerService
     */
    public AppComponentRoot(IRPCProviderService providerService){
        g_providerService=providerService;
    }

    /**
     * 构建消费者
     */
    public AppComponentRoot(){

    }
    public void run(){

    }
}
