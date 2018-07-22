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
            var interfaces = ConfigHelper.Instance.GetComponentConfig().ConsumerConfiguation.Services;
                interfaces.ForEach(service=> {
                    Object serviceImpl = EmitHelper.CreateClass(service.AssemblyName,service.InterfaceName);
                    if (serviceImpl != null)
                    {
                        g_service.GetOrAdd(service.InterfaceName, serviceImpl);
                    }
                });
            
        }

        public static  T createService<T>(String interfaceName)
        {
            if (g_service.ContainsKey(interfaceName)) return (T)g_service[interfaceName];
            return default(T);
        }
    }
}
