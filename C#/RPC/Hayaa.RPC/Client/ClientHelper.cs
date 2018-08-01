using Hayaa.RPC.Common.Config;
using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Hayaa.RPC.Common.Protocol;
using Hayaa.RPC.Service.Util;

namespace Hayaa.RPC.Service.Client
{
    internal class ClientHelper
    {
        private static ClientHelper instance = new ClientHelper();
        private int cpuCoreTotal = 1;//cpu核心数,按照最小计算能力默认
        private ClientHelper()
        {
           
        }
        public static ClientHelper Instance { get => instance; }
        /// <summary>
        /// 构建连接池,进行长链接通信
        /// key:接口
        /// 此种方式适合均衡接口调用，如果有高频函数，则性能有所损失
        /// </summary>
        private Dictionary<String, TcpClient> g_ClientPool = null;
        /// <summary>
        /// 放置某一个接口请求队列过长造成其他接口堆积
        /// </summary>
        private Dictionary<int, ConcurrentQueue<MethodMessage>> g_MethodQueue = null;
        /// <summary>
        /// msgID作为key，远程返回结果作为value
        /// </summary>
        private ConcurrentDictionary<String,ResultMessage> g_ResultDic = null;
        internal void Init()
        {
            g_ClientPool = new Dictionary<string, TcpClient>();
            g_MethodQueue = new Dictionary<int, ConcurrentQueue<MethodMessage>>();
            g_ResultDic = new ConcurrentDictionary<string, ResultMessage>();
            InitNetClient(g_MethodQueue, g_ClientPool);
        }
        private void InitNetClient(Dictionary<int, ConcurrentQueue<MethodMessage>> queue, Dictionary<string, TcpClient> cliPool)
        {
            var config = ConfigHelper.Instance.GetComponentConfig().ConsumerConfiguation.Services;
            for(var i = 0; i < cpuCoreTotal; i++)
            {
                if (!queue.ContainsKey(i))
                {
                    queue.Add(i, new ConcurrentQueue<MethodMessage>());
                }
            }
            config.ForEach(c =>
            {              
                if (!cliPool.ContainsKey(c.InterfaceName))
                {
                    try
                    {
                        cliPool.Add(c.InterfaceName, new TcpClient(c.ServerHost, c.ServerPort)); 
                    }
                    catch(Exception ex)
                    {
                        Console.WriteLine(ex.Message);
                    }
                }
            });
            ThreadPool.SetMaxThreads(cpuCoreTotal, config.Count);
            ThreadPool.QueueUserWorkItem(Consume);
        }
        private  void Consume(Object param)
        {
            if (cpuCoreTotal > 1)//支持多线程处理
            {
                Parallel.For(0, cpuCoreTotal, i => ConsumenQueue(i));
            }
            else 
            {
                ConsumenQueue(0);
            }
        }
        private void ConsumenQueue(int index)
        {
            var queue = g_MethodQueue[index];
            if (queue == null) return;
            MethodMessage methodMessage;
            while (!queue.IsEmpty)
            {
                if(queue.TryPeek(out methodMessage))
                {
                    Transfer(methodMessage);
                }                    
            }
        }       
        private void Transfer(MethodMessage methodMessage)
        {
            var tcp = g_ClientPool[methodMessage.InterfaceName];
            var list = NetPackageHepler.UnPack(methodMessage, methodMessage.MsgID);
            NetworkStream stream = tcp.GetStream();
            //将数据分包发送
            list.ForEach(datapack =>
            {
                Byte[] data = System.Text.Encoding.ASCII.GetBytes(JsonHelper.SerializeObject(datapack));
                stream.Write(data, 0, data.Length);

            });  
            byte[] buffer = new byte[tcp.ReceiveBufferSize];          
            try
            {
                stream.ReadAsync(buffer, 0, buffer.Length);
            }catch(Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            String responseData = null;
            stream.Close();
            if (buffer.Length > 0)
            {
                responseData = System.Text.Encoding.ASCII.GetString(buffer, 0, buffer.Length);
            }
            try
            {
                var resultData = JsonHelper.DeserializeObject<ResultMessage>(responseData);
                if (!g_ResultDic.ContainsKey(resultData.MsgID))
                {
                    g_ResultDic.TryAdd(resultData.MsgID, resultData);
                }
            }catch(Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }
        public void EnQueue(MethodMessage methodMessage)
        {
            int index = methodMessage.InterfaceName.GetHashCode() % cpuCoreTotal;
            g_MethodQueue[index].Enqueue(methodMessage);

        }
        public ResultMessage GetResult(String msgID)
        {
           return g_ResultDic[msgID];
        }
    }
}
