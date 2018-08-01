package hayaa.rpc.common.protocol;

import java.util.Arrays;

/**
 * 自定义的协议
 *  数据包格式
 * +——----——+——-----——+——----——+
 * |协议开始标志|  长度     |   数据       |
 * +——----——+——-----——+——----——+
 * 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76
 * 2.传输数据的长度contentLength，int类型
 * 3.要传输的数据
 */
public class RpcProtocol {
    /**
     * 消息的开头的信息标志
     */
    private int head_data = 0x76;
    /**
     * 消息的长度
     */
    private int contentLength;
    /**
     * 消息的内容
     */
    private byte[] content;

    /**
     * 用于初始化，RpcProtocol
     *
     * @param content
     *            协议里面，消息的数据
     */
    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    public int getHead_data() {
        return head_data;
    }

    public int getContentLength() {
        return contentLength;
    }


    public byte[] getContent() {
        return content;
    }


    @Override
    public String toString() {
        return "RpcProtocol [head_data=" + head_data + ", contentLength="
                + contentLength + ", content=" + Arrays.toString(content) + "]";
    }
}
