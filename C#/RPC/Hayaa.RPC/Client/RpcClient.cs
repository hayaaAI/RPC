using Hayaa.RPC.Common.Config;
using System;
using System.Collections.Generic;
using System.Linq;


namespace Hayaa.RPC.Service.Client
{
    /// <summary>
    /// rpc客户端主类
    /// </summary>
    public class RpcClient
    {
        public  void Run()
        {
            var consumerConfig = ConfigHelper.Instance.GetComponentConfig().ConsumerConfiguation;          
            ServiceFactory.initService(consumerConfig.Services);
            ClientHelper.Instance.Init(consumerConfig.Services);
        }
    }
}
