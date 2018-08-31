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
             var result= ProviderFactory.ExecuteMethod(methodMessage);
            return JsonHelper.SerializeObject(result,true);
        }
    }
}
