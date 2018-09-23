package test.rpc;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.config.RpcConfig;
import org.junit.Test;

import java.util.ArrayList;

public class ConfigTest {
    @Test
    public void getConfigString() {
        RpcConfig rpcConfig = new RpcConfig();
        rpcConfig.setKeepUrl("keepurl");
        rpcConfig.setServicePubliceUrl("publicurl");
        rpcConfig.setSessionTimeout(3 * 1000);
        RpcConfig.ConsumerConfig consumerConfig =rpcConfig.new ConsumerConfig();
        consumerConfig.setMessageSize(1024 * 1024);
        consumerConfig.setServices(new ArrayList<>());
        rpcConfig.setConsumerConfiguation(consumerConfig);
        RpcConfig.ServiceConfig demo = rpcConfig.new ServiceConfig();
        demo.setGroup("group");
        demo.setInterfaceName("test.myrpc");
        demo.setServerHost("127.0.0.1");
        demo.setServerPort(8089);
        demo.setName("default");
        consumerConfig.getServices().add(demo);
        String s = JsonHelper.SerializeObject(rpcConfig);
        RpcConfig rpcConfig1 =JsonHelper.gsonDeserialize(s,RpcConfig.class);
        rpcConfig.setConsumerConfiguation(null);
        RpcConfig.ProviderConfig providerConfig = rpcConfig.new ProviderConfig();
        providerConfig.setGroup("group");
        providerConfig.setMessageSize(1024 * 1024);
        providerConfig.setName("default");
        providerConfig.setPort(8089);
        rpcConfig.setProviderConfiguation(providerConfig);
        s = JsonHelper.SerializeObject(rpcConfig);
    }

}
