package hayaa.rpc.common.protocol;

import java.io.Serializable;
import java.util.Date;

/**
 * 由于java的Gson序列化对于大小写敏感，所以字段命名和其他平台一致
 * @author hsieh
 */
public class ResultMessage implements Serializable {
    private String MsgID;
    private String InterfaceName;
    private String Method;
    private String Result;
    private String ErrMsg;

    public ResultMessage(){

    }

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

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}
