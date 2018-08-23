using Hayaa.RPC.Common.Config;
using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Linq;

namespace Hayaa.RPC.Service.Server
{
    class RpcServer
    {
      
        public void Run()
        {
            var providerConfig = ConfigHelper.Instance.GetComponentConfig().ProviderConfiguation;
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
            //------------------NIO服务器实现TODO-------------------//
           
        }

       
    }
}
