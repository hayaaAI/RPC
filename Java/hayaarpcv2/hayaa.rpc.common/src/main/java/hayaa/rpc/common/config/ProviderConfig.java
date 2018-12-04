package hayaa.rpc.common.config;

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
