using Hayaa.NetNio.Service;
using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Server
{
    internal class BoundHandlerProvider : BoundHandler
    {
        public string Excute(string paramater)
        {
            MethodMessage methodMessage = JsonHelper.Deserialize<MethodMessage>(paramater,true);
            ResultMessage result = null;
            try
            {
                result = ProviderFactory.ExecuteMethod(methodMessage);
            }catch(Exception ex)
            {
                result.MsgID = methodMessage.MsgID;
                result.ErrMsg = ex.Message;
                Console.WriteLine(ex.Message);
            }
            return JsonHelper.SerializeObject(result,true);
        }
    }
}
