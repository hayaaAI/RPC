using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Protocol
{
    /// <summary>
    /// 网络数据分包容器
    /// 将每次数据发送限制在1024byte
    /// </summary>
    public class NetPackage
    {
        public String MsgID { set; get; }
        /// <summary>
        /// 数据包序列
        /// </summary>
        public int MsgIndex { set; get; }       
        /// <summary>
        /// 数据最大长度1024
        /// </summary>
        public byte[] MsgData { set; get; }
    }
}
