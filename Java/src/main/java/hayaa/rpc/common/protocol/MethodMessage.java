package hayaa.rpc.common.protocol;

import java.io.Serializable;
import java.util.HashMap;

public class MethodMessage  implements Serializable {
    private String interfaceName;
    private String method;
    private HashMap<String,Object> paramater;
    private String msgID;
    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }
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
