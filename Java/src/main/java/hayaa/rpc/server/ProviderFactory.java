package hayaa.rpc.server;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ProviderFactory {
    public static ResultMessage executeMethod(MethodMessage methodMessage) {
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setMsgID(methodMessage.getMsgID());
        Method method = getService(methodMessage.getInterfaceName(), methodMessage.getMethod());
        if (method != null) {
            try {
                Object resultObj = method.invoke(getServiceInstance(methodMessage.getInterfaceName()), getParamaters(methodMessage.getParamater()));
                if (resultObj != null) {
                    resultMessage.setResult(JsonHelper.SerializeObject(resultObj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                resultMessage.setErrMsg("服务端异常:"+e.getMessage());
                e.printStackTrace();
            }
        } else {
            resultMessage.setErrMsg("服务端无指定服务函数:" + methodMessage.getInterfaceName() + "." + methodMessage.getMethod());
        }
        return resultMessage;
    }

    /**
     * 扫描服务所在jar
     *
     * @param jarPaths 需要扫描的jar路径
     */
    public synchronized static void ScanServices(List<String> jarPaths) {

    }

    private static Hashtable<String, ProviderContainer> services = new Hashtable<>();

    private static List<Object> getParamaters(Hashtable<String, Object> paramater) {
        List<Object> result = null;
        if (paramater.size() > 0) {
            result = new ArrayList<>();
            for (int i = 0; i < paramater.size(); i++) {
                //保证参数顺序和客户端一致
                Object p = paramater.get("args" + i);
                if (p != null) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * 获取需要执行的函数对象
     *
     * @param interfaceName 接口名称
     * @param methodName    函数名称
     * @return
     */
    private static Method getService(String interfaceName, String methodName) {
        Method method = null;
        if (services.containsKey(interfaceName)) {
            Hashtable<String, Method> methods = services.get(interfaceName).getMethods();
            if ((methods != null) && methods.containsKey(methodName)) {
                method = methods.get(methodName);
            }
        }
        return method;
    }

    /**
     * 获取服务实例
     *
     * @param interfaceName 接口名称
     * @return
     */
    private static Object getServiceInstance(String interfaceName) {
        Object instance = null;
        if (services.containsKey(interfaceName)) {
            instance = services.get(interfaceName).getServiceInstance();
        }
        return instance;
    }
}
