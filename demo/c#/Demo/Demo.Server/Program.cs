using Hayaa.ConfigSeed.Standard;
using Hayaa.RPC.Service.Server;
using System;

namespace Demo.Server
{
    class Program
    {
        static void Main(string[] args)
        {
            AppSeed.Instance.InitConfig();
            RpcServer rpcServer = new RpcServer();
            rpcServer.Run();
        }
    }
}
