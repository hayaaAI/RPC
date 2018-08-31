using Hayaa.RPC.Common.Protocol;
using Hayaa.RPC.Service.Protocol;
using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Server
{
    class RpcDataHelper
    {
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
                Type type = ProviderFactory.GetType(rpcDataValue.ClassName);
                result = JsonHelper.DeserializeObject(rpcDataValue.ValContainer, type);
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
                        result = short.Parse(rpcDataValue.ValContainer);
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
