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
        /// <summary>
        /// key:接口全名称
        /// value:服务实例
        /// </summary>
        private static ConcurrentDictionary<String, Object> g_service = new ConcurrentDictionary<String, Object>();
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

        public static T CreateService<T>(String interfaceFullName)
        {
            if (g_service.ContainsKey(interfaceFullName)) return (T)g_service[interfaceFullName];
            return default(T);
        }
    }
}
