using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;
using System.Linq;
using System.Reflection.Emit;
using Newtonsoft.Json;
using Hayaa.RPC.Service.Util;
using Hayaa.RPC.Service.Protocol;

namespace Hayaa.RPC.Service.Client
{
    /// <summary>
    /// 作为技术研究遗产放弃，但是不删除此类
    /// </summary>
    public class EmitHelper
    {
        private static ModuleBuilder g_modBuilder = null;
        private static AssemblyBuilder g_assyBuilder = null;
        static EmitHelper()
        {
            //System.Reflection.AssemblyName 是用来表示一个Assembly的完整名称的
            AssemblyName assyName = new AssemblyName();
            //为要创建的Assembly定义一个名称（忽略版本号，Culture等信息）
            assyName.Name = "Hayaa.RPCProxy";
            g_assyBuilder = AssemblyBuilder.DefineDynamicAssembly(assyName, AssemblyBuilderAccess.RunAndCollect);
            //获取ModuleBuilder，提供String参数作为Module名称
            g_modBuilder = g_assyBuilder.DefineDynamicModule("RPCProxy_RemoteServic");
        }
        public static Type CreateClass(String assemblyName, String interfaceName)
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
            String className = interfaceName + "Hayaa_ProxyClass";
            // 新类型的属性：要创建的是Class，而非Interface，Abstract Class等，而且是Public的
            TypeAttributes newTypeAttribute = TypeAttributes.Class | TypeAttributes.Public;
            //声明要创建的新类型的父类型
            Type newTypeParent;
            //声明要创建的新类型要实现的接口
            Type[] newTypeInterfaces;
            Type interfaceType = interfaceAssembly.GetType(interfaceName);
            if (!interfaceType.IsInterface) return null;
            newTypeParent = null;
            newTypeInterfaces = new Type[] { interfaceType };
            //得到类型生成器            
            TypeBuilder typeBuilder = g_modBuilder.DefineType(className, newTypeAttribute, newTypeParent, newTypeInterfaces);
            //以下将为新类型声明方法
            //得到基类型的所有方法
            MethodInfo[] targetMethods = interfaceType.GetMethods();
            foreach (MethodInfo targetMethod in targetMethods)
            {
                //得到方法的各个参数的类型
                ParameterInfo[] paramInfo = targetMethod.GetParameters();
                Type[] paramType = new Type[paramInfo.Length];
                for (int i = 0; i < paramInfo.Length; i++)
                    paramType[i] = paramInfo[i].ParameterType;
                //  DynamicMethod targetDynamicMethod = new DynamicMethod(targetMethod.Name, targetMethod.ReturnType, paramType);
                //传入方法签名，得到方法生成器
                MethodBuilder methodBuilder = typeBuilder.DefineMethod(targetMethod.Name, MethodAttributes.Public | MethodAttributes.Virtual, targetMethod.ReturnType, paramType);
                //由于要生成的是具体类，所以方法的实现是必不可少的。而方法的实现是通过Emit IL代码来产生的
                //得到IL生成器                

                ILGenerator ilGen = methodBuilder.GetILGenerator();
                var dicType = typeof(Dictionary<string, RpcDataValue>);
                ilGen.Emit(OpCodes.Nop);
                //声明Dictionary<String, Protocol.RpcDataValue>实例
                LocalBuilder dicObj = ilGen.DeclareLocal(dicType); //局部变量列表索引0
                ilGen.Emit(OpCodes.Newobj, dicType);
                //存储dicObj到局部变量列表索引0处
                ilGen.Emit(OpCodes.Stloc_0);
                int varIndex = 0;//声明变量索引
                var rpcDataHelperType = typeof(RpcDataHelper);
                var parseArgsToDataMethod = rpcDataHelperType.GetMethod("ParseArgsToData");
                var paraList = targetMethod.GetParameters();
                var addMethod = dicType.GetMethod("Add");
                Type[] optionalParameterTypes = new Type[] { typeof(String), typeof(Object) };
                if (paraList != null)
                {
                    for (var i = 0; i < paraList.Length; i++)
                    {

                        //声明RpcDataValue的变量
                        LocalBuilder rpcDataVal = ilGen.DeclareLocal(typeof(RpcDataValue));
                        varIndex++;//索引推进
                        ilGen.Emit(OpCodes.Stloc_S, varIndex);//存储rpcDataVal到局部变量列表索引varIndex处
                        ilGen.Emit(OpCodes.Ldarg_S, varIndex);//将rpcDataVal从局部变量列表索引varIndex处取出
                        ilGen.Emit(OpCodes.Call, parseArgsToDataMethod);//执行转换函数                       
                        ilGen.Emit(OpCodes.Stloc_S, varIndex);//将转换结果存储到局部变量列表索引varIndex处
                        ilGen.Emit(OpCodes.Ldloc_0);//将dicObj变量从索引0处取出
                        ilGen.Emit(OpCodes.Ldstr, "arg" + i);//加载dicObj的key
                        ilGen.Emit(OpCodes.Ldloc_S, varIndex);//将rpcDataVal从局部变量列表索引varIndex处取出
                        ilGen.Emit(OpCodes.Callvirt, addMethod);//将rpcDataVal放入dicObj                       
                        ilGen.Emit(OpCodes.Nop); //Add操作后添加nop间隔
                    }
                }
                ilGen.Emit(OpCodes.Ldstr, interfaceType.Name);
                ilGen.Emit(OpCodes.Ldstr, targetMethod.Name);
                ilGen.Emit(OpCodes.Ldloc_0);//将dicObj变量从索引0处取出
                ilGen.Emit(OpCodes.Call, typeof(ServiceMethdoProxy).GetMethod("Invoke", new Type[] { typeof(String), typeof(String), dicType }));
                LocalBuilder invokeObj = ilGen.DeclareLocal(targetMethod.ReturnType);
                varIndex++;//索引推进
                ilGen.Emit(OpCodes.Stloc_S, varIndex);//Invoke的结果存储变量表的varIndex
                ilGen.Emit(OpCodes.Ldloc_S, varIndex);//从局部变量列表索引varIndex处取出
                LocalBuilder resultObj = ilGen.DeclareLocal(targetMethod.ReturnType);
                varIndex++;//索引推进，指向return的变量
                ilGen.Emit(OpCodes.Stloc_S, varIndex);
                ilGen.Emit(OpCodes.Br_S);
                ilGen.Emit(OpCodes.Ldloc_S, varIndex);
                ilGen.Emit(OpCodes.Ret);
            }
            return typeBuilder.CreateType();
        }
        private static bool IsBox(Type parameterType)
        {
            if (parameterType.IsValueType || parameterType.IsGenericParameter || parameterType.IsGenericType)
            {
                return true;
            }
            return false;
        }
    }
}
