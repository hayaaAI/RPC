package hayaa.rpc.common;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.RpcDataValue;
import hayaa.rpc.server.ServerClassDataStore;

public class RpcDataHelper {

    public static int parseDataType(Object arg) {
        Class<?> clazz = arg.getClass();
        int result = 1;
        switch (clazz.getName()) {
            case "java.lang.String":
                result = 2;
                break;
            case "java.lang.Integer":
                result = 3;
                break;
            case "java.lang.Float":
                result = 4;
                break;
            case "java.lang.Double":
                result = 5;
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
        Class classObj = ServerClassDataStore.getClass(rpcDataValue.getClassName());
        if(classObj!=null){
            result=JsonHelper.gsonDeserialize(rpcDataValue.getValContainer(),classObj);
        }
        return result;
    }
}
