package hayaa.rpc.common.protocol;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

/**
 * 由于java的Gson序列化对于大小写敏感，所以字段命名和其他平台一致
 * @author hsieh
 */
public class MethodMessage  implements Serializable {
    private String msgID;
    private String interfaceName;
    private String method;
    private List<RpcDataValue> paramater;

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

    public List<RpcDataValue> getParamater() {
        return paramater;
    }

    public void setParamater(List<RpcDataValue> paramater) {
        this.paramater = paramater;
    }
}
