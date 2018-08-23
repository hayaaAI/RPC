package hayaa.rpc.common;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.CommunicationPrimitives;
import hayaa.rpc.common.protocol.RpcDataValue;
import hayaa.rpc.server.ServerClassDataStore;

import java.math.BigDecimal;

/**
 * @author hsieh
 */
public class RpcDataHelper {

    /**
     * 获取参数变量的多平台通用数据类型
     * @param arg 方法参数变量
     * @return
     */
    public static int parseDataType(Object arg) {
        Class<?> clazz = arg.getClass();
        //默认为可定义类
        int result = CommunicationPrimitives.CLASS;
        switch (clazz.getName()) {
            case "java.lang.String":
                result = CommunicationPrimitives.String;
                break;
            case "java.lang.Boolean":
                result = CommunicationPrimitives.Boolean;
                break;
            case "java.lang.Byte":
                result = CommunicationPrimitives.Byte;
                break;
            case "java.lang.Short":
                result = CommunicationPrimitives.Short;
                break;
            case "java.lang.Integer":
                result = CommunicationPrimitives.Integer;
                break;
            case "java.lang.Long":
                result = CommunicationPrimitives.Long;
                break;
            case "java.lang.Float":
                result = CommunicationPrimitives.Float;
                break;
            case "java.lang.Double":
                result = CommunicationPrimitives.Double;
                break;
            case "java.lang.BigDecimal":
                result = CommunicationPrimitives.Decimal;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 服务端参数数据转换
     *
     * @param rpcDataValue
     * @return
     */
    public static Object parseServerData(RpcDataValue rpcDataValue) {
        Object result = null;
        Class classObj = null;
        if (rpcDataValue.getDataType().intValue() == CommunicationPrimitives.CLASS) {
            classObj = ServerClassDataStore.getClass(rpcDataValue.getClassName());
            if (classObj != null) {
                result = JsonHelper.gsonDeserialize(rpcDataValue.getValContainer(), classObj);
            }
        } else {
            switch (rpcDataValue.getDataType()) {
                case CommunicationPrimitives.String:
                    result = rpcDataValue.getValContainer();
                    break;
                case CommunicationPrimitives.Boolean:
                    result = Boolean.parseBoolean(rpcDataValue.getValContainer());
                    break;
                case CommunicationPrimitives.Byte:
                    result = Byte.parseByte(rpcDataValue.getValContainer());
                    break;
                case CommunicationPrimitives.Short:
                    result = rpcDataValue.getValContainer();
                    break;
                case CommunicationPrimitives.Integer:
                    result = Integer.parseInt(rpcDataValue.getValContainer());
                    break;
                case CommunicationPrimitives.Long:
                    result = Long.parseLong(rpcDataValue.getValContainer());
                    break;
                case CommunicationPrimitives.Float:
                    result = Float.parseFloat(rpcDataValue.getValContainer());
                    break;
                case CommunicationPrimitives.Double:
                    result = Double.parseDouble(rpcDataValue.getValContainer());
                    break;
                case CommunicationPrimitives.Decimal:
                    result = new BigDecimal(rpcDataValue.getValContainer());
                    break;
                default:
                    result=null;
                    break;
            }
        }
        return result;
    }
}
