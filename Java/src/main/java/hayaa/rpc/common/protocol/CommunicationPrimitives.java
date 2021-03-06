package hayaa.rpc.common.protocol;

/**
 * 通讯原语
 * @author hsieh
 */
public class CommunicationPrimitives {
    /**
     * 无返回数据的网络服务层通知标识
     */
    public final static String NO_DATA ="<EOF>";
    /**
     * 协议分隔头TAG
     */
    public final static byte[] PROTOCOLHEADERTAG={(byte) 0xaa,(byte) 0xbb};
    public final static int CLASS=1;
    public final static int String=2;
    public final static int Boolean=3;
    public final static int Byte=4;
    public final static int Short=5;
    public final static int Integer=6;
    public final static int Long=7;
    public final static int Float=8;
    public final static int Double=9;
    public final static int Decimal=10;
}
