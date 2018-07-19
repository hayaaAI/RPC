package hayaa.rpc.common.config;

public class RPCConfigHelper {
    public static RPCConfig.ProviderConfig getProviderConfig(){
        RPCConfig rpcConfig= ConfigHelper.getInstance().GetComponentConfig();
        if((rpcConfig!=null)&&(rpcConfig.getProviderConfiguation()!=null)){
            return  rpcConfig.getProviderConfiguation();
        }
        return null;
    }
    public static RPCConfig.ConsumerConfig getConsumerConfig(){
        RPCConfig rpcConfig= ConfigHelper.getInstance().GetComponentConfig();
        if((rpcConfig!=null)&&(rpcConfig.getConsumerConfiguation()!=null)){
            return  rpcConfig.getConsumerConfiguation();
        }
        return null;
    }
}
