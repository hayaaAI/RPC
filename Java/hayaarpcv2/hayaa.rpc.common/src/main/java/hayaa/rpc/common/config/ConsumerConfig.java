package hayaa.rpc.common.config;

import java.util.List;

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
