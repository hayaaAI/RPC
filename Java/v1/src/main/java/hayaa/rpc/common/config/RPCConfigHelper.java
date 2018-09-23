package hayaa.rpc.common.config;

public class RPCConfigHelper {
    public static RpcConfig.ProviderConfig getProviderConfig(){
        RpcConfig rpcConfig= ConfigHelper.getInstance().GetComponentConfig();
        if((rpcConfig!=null)&&(rpcConfig.getProviderConfiguation()!=null)){
            return  rpcConfig.getProviderConfiguation();
        }
        return null;
    }
    public static RpcConfig.ConsumerConfig getConsumerConfig(){
        RpcConfig rpcConfig= ConfigHelper.getInstance().GetComponentConfig();
        if((rpcConfig!=null)&&(rpcConfig.getConsumerConfiguation()!=null)){
            return  rpcConfig.getConsumerConfiguation();
        }
        return null;
    }
    public static RpcConfig getRPCConfig(){
        RpcConfig rpcConfig= ConfigHelper.getInstance().GetComponentConfig();
        if(rpcConfig!=null){
            return  rpcConfig;
        }
        return null;
    }
}
