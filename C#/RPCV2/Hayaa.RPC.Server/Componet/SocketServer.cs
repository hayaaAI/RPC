using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Linq;
using System.Collections.Concurrent;

namespace Hayaa.NetNio.Service
{
    /// <summary>
    /// Nio通信模式
    /// 数据编码以网络通信的大端为准
    /// </summary>
    public class SocketServer
    {
        public ManualResetEvent allDone = new ManualResetEvent(false);
        private BoundHandler g_boundHandler;
        public SocketServer(BoundHandler boundHandler)
        {
            g_boundHandler = boundHandler;
        }      
        private ConcurrentBag<Socket> socketListeningList = new ConcurrentBag<Socket>();
        /// <summary>
        /// 
        /// </summary>
        /// <param name="ip"></param>
        /// <param name="port"></param>
        /// <param name="requestQueueMax">最大响应队列,默认1000</param>
        public void Run(String ip, int port, int requestQueueMax = 1000)
        {
            ip = ip.Trim();
            IPAddress iPAddress = IPAddress.Parse(ip);
            // IPEndPoint ipe = (ip=="0.0.0.0")? new IPEndPoint(IPAddress.Any, port) : new IPEndPoint(iPAddress, port);
            IPEndPoint ipe = new IPEndPoint(IPAddress.Any, port);
            Socket rootSocket = new Socket(ipe.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            try
            {                
                rootSocket.Bind(ipe);
                rootSocket.Listen(requestQueueMax);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            int workThreads = 0, ioThreads = 0;
            ThreadPool.GetMinThreads(out workThreads, out ioThreads);           
            ThreadPool.SetMaxThreads(workThreads, ioThreads);
            ThreadPool.QueueUserWorkItem(SocketWait);            
            Console.WriteLine("Listen on IP:"+ ((IPEndPoint)rootSocket.LocalEndPoint).Address.MapToIPv4() +"---port:"+ ((IPEndPoint)rootSocket.LocalEndPoint).Port);
            Boolean listenLoop = true;
            while (listenLoop)
            {
                allDone.Reset();
                Console.WriteLine("listenLoop");
                try
                {
                   
                    rootSocket.BeginAccept(new AsyncCallback(AcceptCallback), rootSocket);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }
                allDone.WaitOne();
            }

        }
        private void SocketWait(Object param)
        {
            while (true)
            {
                //Console.WriteLine("SocketWait");
                for (int i = 0; i < socketListeningList.Count; i++)
                {
                    Socket temp = null;
                    if(socketListeningList.TryPeek(out temp)) {
                       if(temp.Poll(3, SelectMode.SelectRead))
                        {
                           
                                Console.WriteLine("Connected："+ temp.Connected);
                                ReadSocket(temp);
                        }
                    }
                }

            }
        }
        public void AcceptCallback(IAsyncResult ar)
        {
            allDone.Set();
            Console.WriteLine("AcceptCallback");
            Socket listener = (Socket)ar.AsyncState;
            Socket handler = listener.EndAccept(ar);
            handler.NoDelay = true;
            AddSocket(handler);
        }
        private void AddSocket(Socket socket)
        {
            socketListeningList.Add(socket);
        }
        private void DelSocket(Socket socket)
        {
            Console.WriteLine("DelSocket");
            socket.Close();
            socket.Dispose();
            socketListeningList.TryTake(out socket);
        }
        private void ReadSocket(Socket socket)
        {
            Console.WriteLine("ReadSocket");
            StateObject state = new StateObject();
            state.workSocket = socket;
            try
            {
                socket.BeginReceive(state.headerBuffer, 0, 2, 0, new AsyncCallback(ReadCallback), state);//首先读取固定长度为2的头部标识
            }catch(Exception ex)
            {
                DelSocket(socket);
                Console.WriteLine(ex.Message);
            }
        }
      
        public void ReadCallback(IAsyncResult ar)
        {
            Console.WriteLine("ReadCallback");          
            StateObject state = (StateObject)ar.AsyncState;
            Socket handler = state.workSocket;
            int bytesRead = handler.EndReceive(ar);
            byte[] tagHeader = new byte[] { (byte)0xaa, (byte)0xbb };
            byte[] dateLengthByte = new byte[4];
            try
            {
                handler.Receive(dateLengthByte);//读取数据长度字段           
                handler.Receive(state.dataTypeBuffer);//读取数据类型字段
            }catch(Exception ex)
            {
                DelSocket(handler);
                Console.WriteLine(ex.Message);
                return;
            }
            int dataType = IntHelper.ByteArrayToInt(state.dataTypeBuffer);
            int dateLength= IntHelper.ByteArrayToInt(dateLengthByte);
            state.dataBuffer = new byte[dateLength];
            handler.BeginReceive(state.dataBuffer, 0, dateLength, 0,
               new AsyncCallback(ReadDataCallback), state);
        }
        public void ReadDataCallback(IAsyncResult ar)
        {
            Console.WriteLine("ReadDataCallback");
            StateObject state = (StateObject)ar.AsyncState;
            Socket handler = state.workSocket;
            int bytesRead = handler.EndReceive(ar);
            byte[] readData = state.dataBuffer.Take(bytesRead).ToArray();//按照实际获取长度，编码其他平台特异处理的干扰
            String msg = EncodingUTF8Helper.ParseBytesToString(readData);
            String resultMsg = g_boundHandler.Excute(msg);
            byte[] data = null;
            if (resultMsg == null) return;
            data = EncodingUTF8Helper.ParseStringToBytes(resultMsg);
            handler.Send(state.headerBuffer);//发送头部标识
            byte[] dateLengthByte =IntHelper.IntToByteArray(data.Length+4);//针对netty基于长度处理粘包逻辑，需要加长度4
            try
            {
                handler.Send(dateLengthByte);//发送数据长度
                handler.Send(state.dataTypeBuffer);//发送数据类型,未经过转换操作，可以直接发回
                handler.Send(data);//发送数据
            }catch(Exception ex)
            {
                DelSocket(handler);
                Console.WriteLine(ex.Message);
            }
        }       
      
        public class StateObject
        {
            // Client  socket.  
            public Socket workSocket = null;
            public byte[] dataBuffer =null;
            public byte[] headerBuffer = new byte[2];
            public byte[] dataTypeBuffer = new byte[4];           
        }


    }
}
