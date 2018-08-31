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
        public static ResultMessage Invoke(String interfaceName, String methodName, List<RpcDataValue> paramater)
        {
            ResultMessage result = null;
            try
            {
                String msgID = Guid.NewGuid().ToString("N");               
                int timeOut = RpcClient.GetConfig().SessionTimeout;
                int time = 0;               
                 ClientHelper.Instance.EnQueue(new Protocol.MethodMessage()
                {
                    InterfaceName = interfaceName,
                    Method = methodName,
                    Paramater = paramater,
                    MsgID = msgID
                });
               // Console.WriteLine("result wait timeOut:" + timeOut);
                while (time<timeOut)
                {
                    Thread.SpinWait(5000);
                    result = ClientHelper.Instance.GetResult(msgID);
                    if (result != null)
                    {
                        Console.WriteLine("time enough");
                    }
                    if (result != null)
                    {
                        time= timeOut+1;
                    }
                    if (time < timeOut)
                        Thread.SpinWait(1);
                    time = time+3;//考虑代码操作时间增量
                }
                if (result == null)
                {
                    Console.WriteLine("remove msgId:" + msgID);
                    ClientHelper.Instance.DelTimeoutMsgID(msgID);
                }               
                Console.WriteLine("Invoke done");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            return result;
        }

    }
}
