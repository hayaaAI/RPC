﻿using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Reflection;
using System.Linq;
using static Hayaa.RPC.Common.Config.RPCConfig;

namespace Hayaa.RPC.Service.Client
{
    public class RpcServiceFactory
    {
        /// <summary>
        /// key:接口全名称
        /// value:服务实例
        /// </summary>
        private static ConcurrentDictionary<String, Object> g_service = new ConcurrentDictionary<String, Object>();
        public static T CreateService<T>(String interfaceName)
        {
            T t= default(T);
            if (g_service.ContainsKey(interfaceName)) t= (T)g_service[interfaceName];
            return t;
        }
        
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
            if (g_service.IsEmpty)
            {
                throw new Exception("服务对象为空");
            }

        }

        private static Type GetInterfaceType(string assemblyName, string interfaceName)
        {
            var assemblyList = AppDomain.CurrentDomain.GetAssemblies().ToList();
            String basePath = AppDomain.CurrentDomain.BaseDirectory;
            Assembly interfaceAssembly = null;
            assemblyList.ForEach(a =>
            {
                if (a.FullName.Contains(assemblyName))
                {
                    interfaceAssembly = a;
                }
            });
            if (interfaceAssembly == null) {
                
                interfaceAssembly = Assembly.LoadFile(basePath + "/" + assemblyName+".dll");               
            }
            if (interfaceAssembly == null) return null;
            Type interfaceType = interfaceAssembly.GetType(interfaceName);
            return interfaceType;
        }

      
    }
}
