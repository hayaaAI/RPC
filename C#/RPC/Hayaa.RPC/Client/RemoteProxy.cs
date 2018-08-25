using Hayaa.RPC.Service.Protocol;
using ProxyEmitter;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Client
{
    public class RemoteProxy : ProxyBase
    {
        protected override TRet ConvertReturnValue<TRet>(object returnValue)
        {
            if (typeof(TRet) != typeof(int))
                return default(TRet);
            return (TRet)returnValue;
        }

        protected override object Invoke(string interfaceName, string methodName, object[] arguments)
        {
            Dictionary<String, RpcDataValue> argDic =null;
            if (arguments != null)
            {
                argDic = new Dictionary<String, RpcDataValue>();
                for(int i = 0; i < arguments.Length; i++)
                {
                    var temp = new RpcDataValue(arguments[i].GetType(), arguments[i]);
                }
            }
            return ServiceMethdoProxy.Invoke(interfaceName, methodName, null);
        }
    }
}
