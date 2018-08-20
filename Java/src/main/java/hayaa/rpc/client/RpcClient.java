package hayaa.rpc.client;

import hayaa.rpc.common.config.RPCConfigHelper;
import hayaa.rpc.common.config.RpcConfig;

import java.util.List;
import java.util.stream.Collectors;

/**
 * rpc客户端主类
 */
public class RpcClient {
    /**
     * rpc客户端启动入口函数
     */
    public synchronized void run() {
        RpcConfig.ConsumerConfig consumerConfig = RPCConfigHelper.getConsumerConfig();
        List<String> interfaceNameList = consumerConfig.getServices()
                .stream().map(RpcConfig.ServiceConfig::getInterfaceName).collect(Collectors.toList());
        ServiceFactory.initService(interfaceNameList);
        ClientHelper.get_instance().init(consumerConfig);
    }
}
