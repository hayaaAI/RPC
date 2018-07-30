package hayaa.rpc.common.protocol;
/// <summary>
/// 网络数据分包容器
/// 将每次数据发送限制在1024byte
/// </summary>
public class NetPackage {
    private String msgID;
    private int msgIndex;
    private byte[] msgData;

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        this.msgIndex = msgIndex;
    }

    public byte[] getMsgData() {
        return msgData;
    }

    public void setMsgData(byte[] msgData) {
        this.msgData = msgData;
    }
}
