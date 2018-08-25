using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Reflection;
using System.Linq;
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
                Type interfaceType = GetInterfaceType(service.AssemblyName, service.InterfaceName);
                Object serviceImplType = ProxyEmitter.ProxyEmitter.CreateProxy(typeof(RemoteProxy),interfaceType);
                if (serviceImplType != null)
                {
                    g_service.GetOrAdd(service.InterfaceName, serviceImplType);
                }
            });

        }

        private static Type GetInterfaceType(string assemblyName, string interfaceName)
        {
            var assemblyList = AppDomain.CurrentDomain.GetAssemblies().ToList();
            Assembly interfaceAssembly = null;
            assemblyList.ForEach(a =>
            {
                if (a.FullName.Contains(assemblyName))
                {
                    interfaceAssembly = a;
                }
            });
            if (interfaceAssembly == null) interfaceAssembly = Assembly.LoadFrom(assemblyName);
            if (interfaceAssembly == null) return null;
            Type interfaceType = interfaceAssembly.GetType(interfaceName);
            return interfaceType;
        }

        public static T CreateService<T>(String interfaceName)
        {
            if (g_service.ContainsKey(interfaceName)) return (T)g_service[interfaceName];
            return default(T);
        }
    }
}
