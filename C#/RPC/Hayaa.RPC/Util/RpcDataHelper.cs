using Hayaa.RPC.Common.Protocol;
using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Util
{
    class RpcDataHelper
    {
        /// <summary>
        /// 获取参数变量的多平台通用数据类型
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="arg">方法参数变量</param>
        /// <returns></returns>
        public static int parseDataType<T>(T arg)
        {
            Type type = typeof(T);
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
        public static Object parseServerData(RpcDataValue rpcDataValue)
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
    }
}
