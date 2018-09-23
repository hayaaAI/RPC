package hayaa.rpc.client;


import hayaa.rpc.common.config.RpcConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * rpc客户端主类
 */
public class RpcClient {
    private static RpcConfig g_config = null;
    /**
     * rpc客户端启动入口函数
     */
    public synchronized void run(RpcConfig config) {
        g_config = config;
        List<String> interfaceNameList =config.getConsumerConfiguation().getServices().stream().map(RpcConfig.ServiceConfig::getInterfaceName).collect(Collectors.toList());
        ServiceFactory.initService(interfaceNameList);
        ClientHelper.get_instance().init(config.getConsumerConfiguation());
    }
    public static RpcConfig GetConfig()
    {
        return g_config;
    }
}
