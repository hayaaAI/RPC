using Hayaa.RPC.Common.Config;
using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace Hayaa.RPC.Service.Client
{
    public class ServiceMethdoProxy
    {
        public static T Invoke<T>(String interfaceName, String methodName, Dictionary<String, RpcDataValue> paramater)
        {
            T result = default(T);
            try
            {
                String msgID = Guid.NewGuid().ToString("N");
               
                ResultMessage msgResult = null;
                int timeOut = ConfigHelper.Instance.GetComponentConfig().SessionTimeout;
                int time = 0;
                 ClientHelper.Instance.EnQueue(new Protocol.MethodMessage()
                {
                    InterfaceName = interfaceName,
                    Method = methodName,
                    Paramater = paramater,
                    MsgID = msgID
                });
                Console.WriteLine("result wait timeOut:" + timeOut);
                while (time<timeOut)
                {
                    Console.WriteLine("result wait loop");
                    msgResult = ClientHelper.Instance.GetResult(msgID);
                    if (msgResult != null)
                    {
                        time= timeOut+1;
                    }
                    if(time< timeOut)
                    Thread.SpinWait(1);
                    time= time+3;//考虑代码操作时间增量
                }
                if (msgResult != null)
                {
                    if (String.IsNullOrEmpty(msgResult.ErrMsg))
                        result = JsonHelper.Deserialize<T>(msgResult.Result);
                }
                else
                {
                    ClientHelper.Instance.DelTimeoutMsgID(msgID);
                }
                Console.WriteLine("Server result:" + msgResult.Result);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            return result;
        }

    }
}
