using Hayaa.RPC.Common.Protocol;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Protocol
{
    /// <summary>
    /// 自定义的协议
    /// 数据包格式
    /// +——----——+——-----——+——----——+
    /// |   头标识   |  长度       |   数据     |
    /// +——----——+——-----——+——----——+
    /// 1.头部标识:0xaa 0xbb
    /// 2.传输数据的长度contentLength，int类型
    /// 3.要传输的数据
    /// </summary>
    internal class RpcProtocol
    {
      
        /// <summary>
        /// 
        /// </summary>
        /// <param name="content"></param>
        public RpcProtocol(String content)
        {
            this.Data = System.Text.Encoding.UTF8.GetBytes(content);
            this.ContentLength = this.Data.Length + 4;//针对netty的粘包处理进行的特异处理
            this.Type = 1;
            MessageFlag = CommunicationPrimitives.PROTOCOLHEADERTAG;
        }
        public byte[] MessageFlag
        {
            set;get;
        }
        public byte[] Data
        {
            set; get;
        }
        public int ContentLength
        {
            set; get;
        }
        public byte Type
        {
            set;get;
        }

       
    }
}
