using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Protocol
{
    /// <summary>
    /// 自定义的协议
    /// 数据包格式
    /// +——----——+——-----——+——----——+
    /// |协议开始标志|  长度     |   数据       |
    /// +——----——+——-----——+——----——+
    /// 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76
    /// 2.传输数据的长度contentLength，int类型
    /// 3.要传输的数据
    /// </summary>
    internal class RpcProtocol
    {
        private int head_data = 0x76;
        private int contentLength;
        private byte[] content;
        public RpcProtocol(String content)
        {
            this.contentLength = content.Length;
            this.content = System.Text.Encoding.ASCII.GetBytes(content);
        }
        public int HeadData
        {
            get
            {
                return head_data;
            }
        }
        public byte[] Content
        {
            get
            {
                return this.content;
            }
        }
        public int ContentLength
        {
            get
            {
                return this.contentLength;
            }
        }
    }
}
