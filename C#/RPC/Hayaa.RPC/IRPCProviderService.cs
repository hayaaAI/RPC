using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service
{
    public interface IRPCProviderService
    {
        ResultMessage ExecuteMethod(MethodMessage methodMessage);
    }
}
