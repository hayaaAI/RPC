package hayaa.rpc.client;

import java.util.HashMap;
import java.util.UUID;

class ServiceMethdoProxy {
    public static String invoke(String interfaceName, String methodName, HashMap<String, Object> paramater) {
        String result = null;
        try {
            String msgID = UUID.randomUUID().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
