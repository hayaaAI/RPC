using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service
{
    public interface IRPCProviderService
    {
        ExecuteResult executeMethod(String interfaceName, String method, Dictionary<String,Object> parameter);
    }
}
