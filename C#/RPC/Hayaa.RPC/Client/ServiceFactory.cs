using Hayaa.RPC.Common.Config;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Text;
using static Hayaa.RPC.Common.Config.RPCConfig;

namespace Hayaa.RPC.Service.Client
{
    public class ServiceFactory
    {
        private static ConcurrentDictionary<String, Object> g_service = new ConcurrentDictionary<string, object>();
        internal static void initService(List<ServiceConfig> interfaces)
        {

            interfaces.ForEach(service =>
            {
                Type serviceImplType = EmitHelper.CreateClass(service.AssemblyName, service.InterfaceName);
                if (serviceImplType != null)
                {
                    g_service.GetOrAdd(service.InterfaceName, Activator.CreateInstance(serviceImplType));
                }
            });

        }

        public static T CreateService<T>(String interfaceName)
        {
            if (g_service.ContainsKey(interfaceName)) return (T)g_service[interfaceName];
            return default(T);
        }
    }
}
