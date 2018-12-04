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






}
