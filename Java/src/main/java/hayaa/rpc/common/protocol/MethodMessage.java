package hayaa.rpc.common.protocol;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

/**
 * 由于java的Gson序列化对于大小写敏感，所以字段命名和其他平台一致
 * @author hsieh
 */
public class MethodMessage  implements Serializable {
    private String MsgID;
    private String InterfaceName;
    private String Method;
    private List<RpcDataValue> Paramater;

    public String getMsgID() {
        return MsgID;
    }

    public void setMsgID(String msgID) {
        MsgID = msgID;
    }

    public String getInterfaceName() {
        return InterfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        InterfaceName = interfaceName;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public List<RpcDataValue> getParamater() {
        return Paramater;
    }

    public void setParamater(List<RpcDataValue> paramater) {
        Paramater = paramater;
    }
}
