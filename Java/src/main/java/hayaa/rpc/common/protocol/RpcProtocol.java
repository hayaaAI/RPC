package hayaa.rpc.common.protocol;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 自定义的协议
 * 数据包格式
 * +——----——+——----——+——-----——+
 * |   头标识    |   长度    |     数据    |
 * +——----—+——----—+——-----——+
 * 1.头部标识:0xaa 0xbb
 * 2.传输数据的长度contentLength，int类型
 * 3.要传输的数据
 */
public class RpcProtocol {

    /**
     * 消息的长度
     */
    private int contentLength;
    /**
     * 消息的内容
     */
    private byte[] data;
    /**
     * 消息类型
     */
    private int type;
    /**
     * 消息头
     */
    private byte[] messageFlag = new byte[2];
    /**
     * 用于初始化，RpcProtocol
     *
     * @param content 协议里面，消息的数据
     */
    public RpcProtocol(String content) {
        this.data = content.getBytes(Charset.forName("utf-8"));
        this.contentLength = this.data.length+4;
        this.type = 1;
        messageFlag[0] = (byte) 0xaa;
        messageFlag[1] = (byte) 0xbb;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(byte[] messageFlag) {
        this.messageFlag = messageFlag;
    }

    public static String parseData(byte[] data) {
        return new String(data, Charset.forName("utf-8"));
    }
}
