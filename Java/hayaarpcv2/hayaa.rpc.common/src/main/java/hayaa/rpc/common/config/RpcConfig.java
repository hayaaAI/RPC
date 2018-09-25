package hayaa.rpc.common.config;

import java.util.List;

public class RpcConfig {
    private int sessionTimeout;
    private int cpuCore;
    private String serviceRegUrl;
    private String servicePubliceUrl;
    private String keepUrl;
    private ProviderConfig providerConfiguation;
    private ConsumerConfig consumerConfiguation;

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getCpuCore() {
        return cpuCore;
    }

    public void setCpuCore(int cpuCore) {
        this.cpuCore = cpuCore;
    }

    public String getServiceRegUrl() {
        return serviceRegUrl;
    }

    public void setServiceRegUrl(String serviceRegUrl) {
        this.serviceRegUrl = serviceRegUrl;
    }

    public String getServicePubliceUrl() {
        return servicePubliceUrl;
    }

    public void setServicePubliceUrl(String servicePubliceUrl) {
        this.servicePubliceUrl = servicePubliceUrl;
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

    public class ProviderConfig {
        private String name;
        private String group;
        private int port;
        private int messageSize;
        /**
         * 需要扫描的包名称列表，使用逗号分隔,仅对java平台起效
         */
        public String packages;
        /**
         * 此字段对java平台无效
         */
        public String assemblyNames;
        public ProviderConfig() {
            this.name = "defaultService";
            this.group = "defaultGroup";
            this.port = 8080;
            this.messageSize = 1024 * 1024;
        }

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

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getMessageSize() {
            return messageSize;
        }

        public void setMessageSize(int messageSize) {
            this.messageSize = messageSize;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getAssemblyNames() {
            return assemblyNames;
        }

        public void setAssemblyNames(String assemblyNames) {
            this.assemblyNames = assemblyNames;
        }
    }

    public class ConsumerConfig {
        private int messageSize;
        private String name;
        private List<ServiceConfig> services;

        public ConsumerConfig() {
            this.messageSize = 1024 * 1024;
        }

        public int getMessageSize() {
            return messageSize;
        }

        public void setMessageSize(int messageSize) {
            this.messageSize = messageSize;
        }

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

    public class ServiceConfig {
        private String name;
        private String group;
        private String interfaceName;
        private String assemblyName;
        private String serverHost;
        private int serverPort;

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

        public String getAssemblyName() {
            return assemblyName;
        }

        public void setAssemblyName(String assemblyName) {
            this.assemblyName = assemblyName;
        }

        public String getServerHost() {
            return serverHost;
        }

        public void setServerHost(String serverHost) {
            this.serverHost = serverHost;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }
    }
}
