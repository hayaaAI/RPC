using System;

namespace Hayaa.RPC.Common
{
    /// <summary>
    /// 通讯原语
    /// </summary>
    public class CommunicationPrimitives
    {
        //会话开始标识
        public const String START = "HAYAA_SESSION_START";
        //会话结束
        public const String END = "HAYAA_SESSION_END"; 
        //连接中断
        public const String INTERRUPT = "HAYAA_SESSION_INTERRUPT";
        //会话内容闭合标签
        public const String CONTENT = "HAYAA_SESSION_CONETENT";
    }
}
