using Hayaa.RPC.Common.Config;
using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Linq;

namespace Hayaa.RPC.Service.Client
{
    public class ClientHelper
    {
        private static ClientHelper instance = new ClientHelper();
        private ClientHelper()
        {
            g_CliPool = new Dictionary<string, TcpClient>();
            g_queue = new Dictionary<String, ConcurrentQueue<MethodMessage>>();
            init(g_queue, g_CliPool);
        }

        private void init(Dictionary<String, ConcurrentQueue<MethodMessage>> queue, Dictionary<string, TcpClient> cliPool)
        {
            var config = ConfigHelper.Instance.GetComponentConfig().ConsumerConfiguation.Services;
            config.ForEach(c =>
            {
                if (!queue.ContainsKey(c.InterfaceName))
                {
                    queue.Add(c.InterfaceName, new ConcurrentQueue<MethodMessage>());
                }
                if (!cliPool.ContainsKey(c.InterfaceName))
                {

                }
            });
        }

        public static ClientHelper Instance { get => instance; }
        /// <summary>
        /// 构建连接池,进行长链接通信
        /// key:接口
        /// 此种方式适合均衡接口调用，如果有高频函数，则性能有所损失
        /// </summary>
        private Dictionary<String, TcpClient> g_CliPool = null;
        /// <summary>
        /// 放置某一个接口请求队列过长造成其他接口堆积
        /// </summary>
        private Dictionary<String,ConcurrentQueue<MethodMessage>> g_queue = null;

     
    }
}
