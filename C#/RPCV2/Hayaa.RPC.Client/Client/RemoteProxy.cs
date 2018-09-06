using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using ProxyEmitter;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Client
{
    public class RemoteProxy : ProxyBase
    {
        public RemoteProxy()
        {

        }
        protected override TRet ConvertReturnValue<TRet>(String returnValue)
        {
            TRet result = default(TRet);
            if (String.IsNullOrEmpty(returnValue))
                return result;
            Boolean isClass = false;
            result = RpcDataHelper.ParseValueTypeData<TRet>(returnValue, out isClass);
            if (isClass)
            {
                result = JsonHelper.Deserialize<TRet>(returnValue,true);
            }
            return result;
        }

        protected override String Invoke(string interfaceName, string methodName, object[] arguments)
        {
            List<RpcDataValue> argDic =null;
            if (arguments != null)
            {
                argDic = new List<RpcDataValue>();
                for(int i = 0; i < arguments.Length; i++)
                {
                    var temp = new RpcDataValue(arguments[i].GetType(), arguments[i],i);
                    argDic.Add(temp);
                }
            }
            ResultMessage resultMessage= ServiceMethdoProxy.Invoke(interfaceName, methodName, argDic);
            if ((resultMessage==null)||(!String.IsNullOrEmpty(resultMessage.ErrMsg)))
            {
                Console.WriteLine("{0}:{1}:{2}",interfaceName, methodName, (resultMessage == null)?"请求超时":resultMessage.ErrMsg);
                return null;
            }           
            return resultMessage.Result;
        }
    }
}
