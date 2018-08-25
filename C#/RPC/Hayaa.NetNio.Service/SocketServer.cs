using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Hayaa.NetNio.Service
{
    /// <summary>
    /// Nio通信模式
    /// </summary>
    public class SocketServer
    {
        private BoundHandler g_boundHandler;
        public SocketServer(BoundHandler boundHandler)
        {
            g_boundHandler = boundHandler;
        }      
        private List<Socket> socketListeningList = new List<Socket>(1000);
        /// <summary>
        /// 
        /// </summary>
        /// <param name="ip"></param>
        /// <param name="port"></param>
        /// <param name="requestQueueMax">最大响应队列,默认1000</param>
        public void Run(String ip, int port, int requestQueueMax = 1000)
        {
            IPAddress iPAddress = IPAddress.Parse(ip);
            IPEndPoint ipe = new IPEndPoint(iPAddress, port);
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
            ThreadPool.SetMaxThreads(1, 4);
            ThreadPool.QueueUserWorkItem(SocketWait);
            Console.WriteLine("Listen on:" + port);
            Boolean listenLoop = true;
            while (listenLoop)
            {               
                try
                {
                    rootSocket.BeginAccept(new AsyncCallback(AcceptCallback),rootSocket);
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }               
            }

        }
        private void SocketWait(Object param)
        {
            while (true)
            {                            
                for (int i = 0; i < socketListeningList.Count; i++)
                {
                    if (socketListeningList[i].Poll(3, SelectMode.SelectRead))
                    {
                        ReadSocket(socketListeningList[i]);
                    }
                }

            }
        }
        public void AcceptCallback(IAsyncResult ar)
        {
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
            socketListeningList.Remove(socket);
            socket.Close();
            socket.Dispose();
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
            int dateLength = BitConverter.ToInt32(dateLengthByte, 0);
            int dataType = BitConverter.ToInt32(state.dataTypeBuffer, 0);
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
            String msg = Encoding.UTF8.GetString(state.dataBuffer);
            String resultMsg = g_boundHandler.Excute(msg);
            byte[] data = null;
            if (resultMsg == null) return;
            data = Encoding.UTF8.GetBytes(resultMsg);
            handler.Send(state.headerBuffer);//发送头部标识
            byte[] dateLengthByte = BitConverter.GetBytes(data.Length);
            try
            {
                handler.Send(dateLengthByte);//发送数据长度
                handler.Send(state.dataTypeBuffer);//发送数据类型
                handler.Send(data);
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
