using Hayaa.RPC.Common.Config;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Client
{
  public  class ServiceFactory
    {
        private static ConcurrentDictionary<String, Object> g_service = new ConcurrentDictionary<string, object>();
        public static void initService()
        {
            //var interfaces = ConfigHelper.Instance.GetComponentConfig().ConsumerConfiguation.Services;
            List<RPCConfig.ServiceConfig> interfaces = new List<RPCConfig.ServiceConfig>() {
                new RPCConfig.ServiceConfig(){
                     AssemblyName="ClassLibrary1",
                      Group="dev",
                       InterfaceName="ClassLibrary1.Interface1",
                        Name="test"
                }
            };
                interfaces.ForEach(service=> {
                    Type serviceImplType = EmitHelper.CreateClass(service.AssemblyName,service.InterfaceName);
                    if (serviceImplType != null)
                    {
                        g_service.GetOrAdd(service.InterfaceName, Activator.CreateInstance(serviceImplType));
                    }
                });
            
        }

        public static  T CreateService<T>(String interfaceName)
        {
            if (g_service.ContainsKey(interfaceName)) return (T)g_service[interfaceName];
            return default(T);
        }
    }
}
