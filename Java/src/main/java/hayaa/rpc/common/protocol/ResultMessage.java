package hayaa.rpc.common.protocol;

import java.io.Serializable;
import java.util.Date;

public class ResultMessage implements Serializable {
    private String interfaceName;
    private String method;
    private String result;
    private String errMsg;
    private String msgID;
    private Date createTime;

    public ResultMessage(){
        this.createTime=new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }




}
