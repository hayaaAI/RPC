package hayaa.rpc.client;

import hayaa.common.StringUtil;
import hayaa.rpc.common.config.RPCConfigHelper;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

class ServiceMethdoProxy {
    public static String invoke(String interfaceName, String methodName, Hashtable<String, Object> paramater) {
        String result = null;
        String msgID = UUID.randomUUID().toString();
        MethodMessage methodMessage = new MethodMessage();
        methodMessage.setInterfaceName(interfaceName);
        methodMessage.setMethod(methodName);
        methodMessage.setParamater(paramater);
        methodMessage.setMsgID(msgID);
        ResultMessage msgResult = null;
        int timeOut = RPCConfigHelper.getRPCConfig().getSessionTimeout();
        int time = 0;
        try {
            Boolean action = ClientHelper.get_instance().enQueue(methodMessage);
            if (!action) {
                return result;
            }
            while (time < timeOut) {
                msgResult = ClientHelper.get_instance().GetResult(msgID);
                if (msgResult != null) {
                    time = timeOut + 1;
                }
                if (time < timeOut) {
                    Thread.sleep(1);
                }
                //考虑代码操作时间增量
                time = time + 3;
            }
            if (StringUtil.IsNullOrEmpty(msgResult.getErrMsg())) {
                result = msgResult.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
