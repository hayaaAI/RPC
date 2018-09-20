using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using Hayaa.RPC.Service.Util;
using static Hayaa.RPC.Common.Config.RPCConfig;

namespace Hayaa.RPC.Service.Client
{
    /// <summary>
    /// 数据编码以网络通信的大端为准
    /// </summary>
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
        private ConcurrentDictionary<String, Boolean> g_ResultDicTag = null;
        private List<ServiceConfig> g_Config;
        public ManualResetEvent allDone = new ManualResetEvent(false);
        internal void Init(List<ServiceConfig> config)
        {
            g_Config = config;
            g_ClientPool = new Dictionary<string, TcpClient>();
            g_MethodQueue = new Dictionary<int, ConcurrentQueue<MethodMessage>>();
            g_ResultDic = new ConcurrentDictionary<string, ResultMessage>();
            g_ResultDicTag = new ConcurrentDictionary<string, bool>();
            InitNetClient(g_MethodQueue, g_ClientPool);
        }
        private void InitNetClient(Dictionary<int, ConcurrentQueue<MethodMessage>> queue, Dictionary<string, TcpClient> cliPool)
        {
           
            for(var i = 0; i < cpuCoreTotal; i++)
            {
                if (!queue.ContainsKey(i))
                {
                    queue.Add(i, new ConcurrentQueue<MethodMessage>());
                }
            }
            g_Config.ForEach(c =>
            {              
                if (!cliPool.ContainsKey(c.InterfaceName))
                {
                    try
                    {
                        cliPool.Add(c.InterfaceName, new TcpClient(c.ServerHost, c.ServerPort) {NoDelay=true }); 
                    }
                    catch(Exception ex)
                    {
                        Console.WriteLine(ex.Message);
                    }
                }
            });
            int workThreads = 0, ioThreads = 0;
            ThreadPool.GetMinThreads(out workThreads,out ioThreads);
            if(workThreads> cpuCoreTotal)
            {
                workThreads = workThreads + cpuCoreTotal;
            }           
            ThreadPool.SetMaxThreads(cpuCoreTotal, ioThreads);
            ThreadPool.QueueUserWorkItem(Consume);
        }

        internal void DelTimeoutMsgID(string msgID)
        {
            Boolean t = true;
            g_ResultDicTag.TryRemove(msgID, out t);
        }

        private  void Consume(Object param)
        {
            while (true)
            {
                allDone.Reset();
                if (cpuCoreTotal > 1)//支持多线程处理
                {
                    Parallel.For(0, cpuCoreTotal, i => ConsumenQueue(i));
                }
                else
                {
                    ConsumenQueue(0);
                }
                allDone.WaitOne();
            }
        }
        private void ConsumenQueue(int index)
        {
            var queue = g_MethodQueue[index];
            if (queue == null) return;
            MethodMessage methodMessage;
            while (!queue.IsEmpty)
            {
                if (queue.TryDequeue(out methodMessage))
                {
                    Transfer(methodMessage);
                }
            }
        }       
        private void Transfer(MethodMessage methodMessage)
        {
            Console.WriteLine("Transfer-in");
            var tcp = g_ClientPool[methodMessage.InterfaceName];
            String msg = JsonHelper.SerializeObject(methodMessage,true);
            RpcProtocol rpcProtocol = new RpcProtocol(msg);
            if (!tcp.Connected) {
                Console.WriteLine("tcp.Connected:false");
                return; }
            //Console.WriteLine("rpc client send starting");
            NetworkStream stream = tcp.GetStream();
            Console.WriteLine("Transfer 发送请求");
            //写头部标识
            stream.Write(rpcProtocol.MessageFlag,0, rpcProtocol.MessageFlag.Length);
            byte[] dataLength =  IntHelper.IntToByteArray(rpcProtocol.ContentLength);//按照大端数据编码发送
            //写数据长度
            stream.Write(dataLength, 0, dataLength.Length);
            byte[] dataType =  IntHelper.IntToByteArray(rpcProtocol.Type);//按照大端数据编码发送
            //写数据类型
            stream.Write(dataType, 0, dataType.Length);
            //写数据
            stream.Write(rpcProtocol.Data, 0, rpcProtocol.Data.Length);//UTF8字符串无需处理大小端
           // Console.WriteLine("rpc client send end");
            byte[] buffer = null;          
            try
            {
                byte[] header = new byte[2];
                Console.WriteLine("Transfer 读取服务数据");
                // Console.WriteLine("rpc client read starting");
                //stream.ReadAsync(header, 0, header.Length); //使用此方法在存在奇怪的本地IO抢夺引起延迟   
                stream.Read(header, 0, header.Length);
                dataLength = new byte[4];
                //长度是4个字节的数据长度,按照大端读取
                dataLength[0]=(byte)stream.ReadByte();
                dataLength[1] = (byte)stream.ReadByte();
                dataLength[2] = (byte)stream.ReadByte();
                dataLength[3] = (byte)stream.ReadByte();
                int contentLength = IntHelper.ByteArrayToInt(dataLength);
                //读取类型
                dataType = new byte[4];
                //类型是4个字节的数据长度,按照大端读取
                dataType[0] = (byte)stream.ReadByte();
                dataType[1] = (byte)stream.ReadByte();
                dataType[2] = (byte)stream.ReadByte();
                dataType[3] = (byte)stream.ReadByte();
                buffer = new byte[contentLength];
                //stream.ReadAsync(buffer, 0, buffer.Length);
                stream.Read(buffer, 0, buffer.Length);
                // Console.WriteLine("rpc client read end");
            }
            catch(Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            String responseData = null;
           // stream.Close();
            if ((buffer!=null)&&(buffer.Length > 0))
            {
                responseData = System.Text.Encoding.UTF8.GetString(buffer, 0, buffer.Length);
            }
            try
            {
                var resultData = JsonHelper.Deserialize<ResultMessage>(responseData,true);
                if (!g_ResultDic.ContainsKey(resultData.MsgID))
                {
                    Console.WriteLine("put result");
                    g_ResultDic.TryAdd(resultData.MsgID, resultData);
                }
            }catch(Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            Console.WriteLine("Transfer-out");
        }
        public void EnQueue(MethodMessage methodMessage)
        {
            allDone.Set();
            int index = methodMessage.InterfaceName.GetHashCode() % cpuCoreTotal;
            g_MethodQueue[index].Enqueue(methodMessage);
            g_ResultDicTag.TryAdd(methodMessage.MsgID, true);

        }
        public ResultMessage GetResult(String msgID)
        {
            ResultMessage result = null;
            g_ResultDic.TryGetValue(msgID, out result);
            if (result != null)
            {
                Boolean t = true;
                g_ResultDicTag.TryRemove(msgID,out t);
            }
           return result;
        }
    }
}
