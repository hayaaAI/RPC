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
            switch (type.FullName)
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
       
        public static T ParseValueTypeData<T>(String valContainer,out Boolean isClass)
        {
            isClass = false;
            Object result = null;
            String name = typeof(T).FullName;
            switch (name)
            {
                case "System.String":
                    result = valContainer;
                    break;
                case "System.Boolean":
                    result = Boolean.Parse(valContainer);
                    break;
                case "System.Byte":
                    result = Byte.Parse(valContainer);
                    break;
                case "System.Int16":
                    result = short.Parse(valContainer);
                    break;
                case "System.Int32":
                    result = int.Parse(valContainer);
                    break;
                case "System.Int64":
                    result = long.Parse(valContainer);
                    break;
                case "System.Single":
                    result = float.Parse(valContainer);
                    break;
                case "System.Double":
                    result = double.Parse(valContainer);
                    break;
                case "System.Decimal":
                    result = decimal.Parse(valContainer);
                    break;
                default:
                    break;
            }
            if (result == null) {
                isClass = true;
                return default(T); }
            return (T)result;
        }
    }
}
