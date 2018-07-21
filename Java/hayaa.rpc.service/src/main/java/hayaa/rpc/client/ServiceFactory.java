package hayaa.rpc.client;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {
    private static ConcurrentHashMap<String, Object> g_service = new ConcurrentHashMap<>();

    public static void initService(List<String> interfaces) {
        if (interfaces != null) {
            interfaces.forEach(service -> {
                Object serviceImpl = JavassistHelper.createClass(service);
                if(serviceImpl!=null){
                    g_service.put(service,serviceImpl);
                }
            });
        }
    }

    public static <T> T createService(String interfaceName) {
        if (g_service.containsKey(interfaceName)) return (T) g_service.get(interfaceName);
        return null;
    }
}
