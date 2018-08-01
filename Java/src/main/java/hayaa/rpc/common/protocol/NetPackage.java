package hayaa.rpc.common.protocol;

/**
 * 网络数据分包容器
 * 将每次数据发送限制在1024byte
 */
public class NetPackage {
    private String msgID;
    /**
     *数据包序列
     */
    private int msgIndex;
    /**
     *数据最大长度1024byte
     */
    private byte[] msgData;
    /**
     * 数据包总数
     */
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

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
