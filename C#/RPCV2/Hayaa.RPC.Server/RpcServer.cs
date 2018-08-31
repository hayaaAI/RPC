using Hayaa.RPC.Common.Config;
using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Linq;
using Hayaa.NetNio.Service;

namespace Hayaa.RPC.Service.Server
{
    /// <summary>
    /// 每个客户端第1--3次的请求由于程序加载会在3000超时设置下出现超时，所以最好做预请求处理
    /// </summary>
  public  class RpcServer
    {
      
        public void Run(RPCConfig rpcConfig)
        {
            var providerConfig = rpcConfig.ProviderConfiguation;
            if (providerConfig == null)
            {
                Console.WriteLine("配置文件中ProviderConfig节点未配置");
                return;
            }
            String assemblyNames = providerConfig.AssemblyNames;
            if (!String.IsNullOrEmpty(assemblyNames))
            {
                List<String> packageList = assemblyNames.Split(",").ToList();
                ProviderFactory.ScanServices(packageList);
               
            }
            else
            {
                Console.WriteLine("配置文件中ProviderConfig节点packages字段未配置");
            }
            BoundHandler service = new BoundHandlerProvider();
            SocketServer socketServer = new SocketServer(service);
            Console.WriteLine("server run");
            socketServer.Run("0.0.0.0", providerConfig.Port);
        }

       
    }
}
