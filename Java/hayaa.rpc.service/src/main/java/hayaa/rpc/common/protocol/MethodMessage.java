package hayaa.rpc.common.protocol;

import java.io.Serializable;
import java.util.HashMap;

public class MethodMessage  implements Serializable {
    private String interfaceName;
    private String method;
    private HashMap<String,Object> paramater;
    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HashMap<String, Object> getParamater() {
        return paramater;
    }

    public void setParamater(HashMap<String, Object> paramater) {
        this.paramater = paramater;
    }


}
