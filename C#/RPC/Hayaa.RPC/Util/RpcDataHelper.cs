using Hayaa.RPC.Common.Protocol;
using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Util
{
  public  class RpcDataHelper
    {
        /// <summary>
        /// 获取参数变量的多平台通用数据类型
        /// </summary>
        /// <param name="arg">方法参数变量的类型</param>
        /// <returns></returns>
        public static int ParseDataType(Type type)
        {
            int result = CommunicationPrimitives.CLASS;
            switch (type.Name)
            {
                case "System.String":
                    result = CommunicationPrimitives.String;
                    break;
                case "System.Boolean":
                    result = CommunicationPrimitives.Boolean;
                    break;
                case "System.Byte":
                    result = CommunicationPrimitives.Byte;
                    break;
                case "System.Int16":
                    result = CommunicationPrimitives.Short;
                    break;
                case "System.Int32":
                    result = CommunicationPrimitives.Integer;
                    break;
                case "System.Int64":
                    result = CommunicationPrimitives.Long;
                    break;
                case "System.Single":
                    result = CommunicationPrimitives.Float;
                    break;
                case "System.Double":
                    result = CommunicationPrimitives.Double;
                    break;
                case "System.Decimal":
                    result = CommunicationPrimitives.Decimal;
                    break;
                default:
                    break;
            }
            return result;
        }
        /// <summary>
        /// 转换RpcDataValue为封箱状态下的参数变量
        /// </summary>
        /// <param name="rpcDataValue"></param>
        /// <returns></returns>
        public static Object ParseDataToArg(RpcDataValue rpcDataValue)
        {
            Object result = null;
            if (rpcDataValue.DataType == CommunicationPrimitives.CLASS)
            {
                result = JsonHelper.DeserializeObject(rpcDataValue.ValContainer);
            }
            else
            {
                switch (rpcDataValue.DataType)
                {
                    case CommunicationPrimitives.String:
                        result = rpcDataValue.ValContainer;
                        break;
                    case CommunicationPrimitives.Boolean:
                        result = Boolean.Parse(rpcDataValue.ValContainer);
                        break;
                    case CommunicationPrimitives.Byte:
                        result = Byte.Parse(rpcDataValue.ValContainer);
                        break;
                    case CommunicationPrimitives.Short:
                        result = rpcDataValue.ValContainer;
                        break;
                    case CommunicationPrimitives.Integer:
                        result = int.Parse(rpcDataValue.ValContainer);
                        break;
                    case CommunicationPrimitives.Long:
                        result = long.Parse(rpcDataValue.ValContainer);
                        break;
                    case CommunicationPrimitives.Float:
                        result = float.Parse(rpcDataValue.ValContainer);
                        break;
                    case CommunicationPrimitives.Double:
                        result = double.Parse(rpcDataValue.ValContainer);
                        break;
                    case CommunicationPrimitives.Decimal:
                        result = decimal.Parse(rpcDataValue.ValContainer);
                        break;
                    default:
                        result = null;
                        break;
                }
            }
            return result;
        }
        /// <summary>
        /// 将参数变量转换为RpcDataValue
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="t"></param>
        /// <returns></returns>
        public static RpcDataValue ParseArgsToData<T>(T arg)
        {
            Type type =typeof(T);
            RpcDataValue rpcDataValue = new RpcDataValue(type, arg);
            return rpcDataValue;
        }
    }
}
