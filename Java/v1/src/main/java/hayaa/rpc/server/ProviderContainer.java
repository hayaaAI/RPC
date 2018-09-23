package hayaa.rpc.server;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * @author hsieh
 */
public class ProviderContainer {
    private String interfaceName;
    private Object serviceInstance;
    private Hashtable<String, Method> methods;

    public ProviderContainer(String interfaceName,Object serviceInstance, Hashtable<String, Method> methods) {
        this.interfaceName = interfaceName;
        this.serviceInstance=serviceInstance;
        this.methods = methods;
    }

    public Object getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(Object serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Hashtable<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(Hashtable<String, Method> methods) {
        this.methods = methods;
    }
}
