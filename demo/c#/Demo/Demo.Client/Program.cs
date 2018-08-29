using demo.interfaceservice;
using Hayaa.ConfigSeed.Standard;
using Hayaa.RPC.Service.Client;
using System;

namespace Demo.Client
{
    class Program
    {
        static void Main(string[] args)
        {
            AppSeed.Instance.InitConfig();
            RpcClient rpcClient = new RpcClient();
            rpcClient.Run();
            var service = ServiceFactory.CreateService<IService>(typeof(IService).FullName);
            while (true)
            {
                String msg = Console.ReadLine();                
                var data = service.DoRpc(new Paramater() {
                     Title=msg,
                      Val= msg.Length
                });
                if(data!=null)
                Console.WriteLine("服务返回:{0};长度:{1}", data.Title, data.Val);
            }
        }
    }
}
