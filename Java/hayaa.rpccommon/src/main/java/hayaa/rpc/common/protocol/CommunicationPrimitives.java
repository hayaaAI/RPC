package hayaa.rpc.common.protocol;

/**
 * 通讯原语
 */
public class CommunicationPrimitives {
    //会话开始标识
    public final  static String START="HAYAA_SESSION_START";
    //会话结束
    public final static String END="HAYAA_SESSION_END";
    //连接中断
    public final static String INTERRUPT="HAYAA_SESSION_INTERRUPT";
    //会话内容闭合标签
    public final static String CONTENT="HAYAA_SESSION_CONETENT";
}
