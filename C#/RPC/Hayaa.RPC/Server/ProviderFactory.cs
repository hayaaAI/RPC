﻿using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Reflection;
using System.Linq;

namespace Hayaa.RPC.Service.Server
{
    class ProviderFactory
    {
        private static Dictionary<string, ProviderContainer> g_services = new Dictionary<string, ProviderContainer>();
        private static Dictionary<String, Type> g_ObjList = new Dictionary<string, Type>();
        internal static void ScanServices(List<String> assemblyNames)
        {
            assemblyNames.ForEach(assemblyName => {
                var assembly = Assembly.LoadFrom(assemblyName);              
               var types= assembly.GetTypes();
                types.ToList().ForEach(t => {
                    if (!t.IsInterface)
                    {
                        var interfaces = t.GetInterfaces();
                        if ((interfaces != null)&&(interfaces.Length>0))
                        {
                            Object instance = null;
                            try
                            {
                                instance = Activator.CreateInstance(t);
                            }
                            catch(Exception ex)
                            {
                                Console.WriteLine(ex.Message);
                            }
                            if (instance != null)
                            {
                                for(int i = 0; i < interfaces.Length; i++)
                                {
                                    MethodInfo[] methods = interfaces[i].GetMethods();
                                    if (methods != null)
                                    {
                                        var dic = new Dictionary<String, MethodInfo>(methods.Length);
                                        for (int mi = 0; mi < methods.Length; mi++)
                                        {
                                            dic.Add(methods[mi].Name, methods[mi]);
                                        }
                                        ProviderContainer providerContainer = new ProviderContainer(interfaces[i].Name,
                                                instance, dic);
                                        g_services.Add(interfaces[i].FullName, providerContainer);
                                    }
                                }
                            }
                        }
                        else
                        {
                            g_ObjList.Add(t.FullName, t);
                        }
                    }
                });
            });
        }
        internal static Type GetType(String fullName)
        {
            return g_ObjList.ContainsKey(fullName) ? g_ObjList[fullName] : null;
        }
        /// <summary>
        /// 通过反射执行目标接口的函数
        /// </summary>
        /// <param name="methodMessage">消费端发送的参数</param>
        /// <returns></returns>
        public static ResultMessage ExecuteMethod(MethodMessage methodMessage)
        {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.MsgID = methodMessage.MsgID;
            MethodInfo method = GetMethod(methodMessage.InterfaceName, methodMessage.Method);
            if (method != null)
            {
                Object[] methodParamater = GetParamaters(methodMessage.Paramater);
                Object resultObj = null;
                if (methodParamater != null)
                {
                    Object instance = GetServiceInstance(methodMessage.InterfaceName);
                    resultObj = method.Invoke(instance, methodParamater);
                }
                else
                {
                    resultMessage.ErrMsg="方法参数转换失败";
                }
                if (resultObj != null)
                {
                    resultMessage.Result=JsonHelper.SerializeObject(resultObj,true);
                }
            }
            else
            {
                resultMessage.ErrMsg="服务端无指定服务函数:" + methodMessage.InterfaceName + "." + methodMessage.Method;
            }
            return resultMessage;
        }

       

     

        private static object GetServiceInstance(string interfaceName)
        {
            Object instance = null;
            if (g_services.ContainsKey(interfaceName))
            {
                instance = g_services[interfaceName].ServiceInstance;
            }
            return instance;
        }

        private static object[] GetParamaters(List<RpcDataValue> paramater)
        {
            List<Object> result = null;
            if (paramater.Count> 0)
            {
                //保证参数顺序一致
                paramater.Sort((left, right) =>
                {
                    if (left.ArgIndex > right.ArgIndex)
                        return 1;
                    else if (left.ArgIndex == right.ArgIndex)
                        return 0;
                    else
                        return -1;
                });
                result = new List<Object>();
                for (int i = 0; i < paramater.Count; i++)
                {                    
                    RpcDataValue rpcDataValue = paramater[i];                 
                    Object p = RpcDataHelper.ParseDataToArg(rpcDataValue);
                    if (p != null)
                    {
                        result.Add(p);
                    }
                    else
                    {
                        return null;
                    }
                }
            }
            return result.ToArray();
        }

       
        /// <summary>
        /// 获取需要执行的函数对象
        /// </summary>
        /// <param name="interfaceName">接口名称</param>
        /// <param name="methodName">函数名称</param>
        /// <returns></returns>
        private static MethodInfo GetMethod(string interfaceName, string methodName)
        {
            MethodInfo method = null;
            if (g_services.ContainsKey(interfaceName))
            {
                var methods = g_services[interfaceName].Methods;
                if ((methods != null) && methods.ContainsKey(methodName))
                {
                    method = methods[methodName];
                }
            }
            return method;
        }

    }
}
