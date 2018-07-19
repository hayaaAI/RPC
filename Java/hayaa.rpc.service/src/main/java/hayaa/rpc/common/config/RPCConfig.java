package hayaa.rpc.common.config;

import hayaa.basemodel.model.ConfigContent;

import java.util.List;

public class RPCConfig extends ConfigContent {
    //服务注册地址
    private String serviceReg;
    //服务发布地址
    private String servicePublice;
    //心跳服务地址
    private String keepUrl;
    //服务端口
    private Integer port;
    public String getServiceReg() {
        return serviceReg;
    }

    public void setServiceReg(String serviceReg) {
        this.serviceReg = serviceReg;
    }

    public String getServicePublice() {
        return servicePublice;
    }

    public void setServicePublice(String servicePublice) {
        this.servicePublice = servicePublice;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getKeepUrl() {
        return keepUrl;
    }

    public void setKeepUrl(String keepUrl) {
        this.keepUrl = keepUrl;
    }

    public ProviderConfig getProviderConfiguation() {
        return providerConfiguation;
    }

    public void setProviderConfiguation(ProviderConfig providerConfiguation) {
        this.providerConfiguation = providerConfiguation;
    }

    public ConsumerConfig getConsumerConfiguation() {
        return consumerConfiguation;
    }

    public void setConsumerConfiguation(ConsumerConfig consumerConfiguation) {
        this.consumerConfiguation = consumerConfiguation;
    }
    private ProviderConfig providerConfiguation;

    private ConsumerConfig consumerConfiguation;

    /**
     * 服务提供者配置
     */
    public class ProviderConfig {
        private String name;
        //服务组
        private String group;

        public String getGroup() {
            return group;
        }
        public void setGroup(String group) {
            this.group = group;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 消费者配置
     */
    public class ConsumerConfig {
        private String name;
        private List<ServiceConfig> services;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ServiceConfig> getServices() {
            return services;
        }

        public void setServices(List<ServiceConfig> services) {
            this.services = services;
        }
    }

    /**
     * 服务配置
     */
    public class ServiceConfig {
        private String name;
        private String group;
        private String interfaceName;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public void setInterfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
        }
    }
}
