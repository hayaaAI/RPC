package hayaa.rpc.server;

import hayaa.common.JsonHelper;
import hayaa.common.PackageScanHelper;
import hayaa.rpc.common.RpcDataHelper;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;
import hayaa.rpc.common.protocol.RpcDataValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 此类中方法必须首先执行ScanServices函数
 */
class ProviderFactory {
    private static Hashtable<String, ProviderContainer> services = new Hashtable<>(100);
    /**
     * 扫描服务所在package
     *·
     * @param packageNames 需要扫描的package名字
     */
    public synchronized static void scanServices(List<String> packageNames) {
        packageNames.forEach(packageName -> {
            List<Class<?>> classList= PackageScanHelper.scan(packageName);
            if(classList!=null){
                classList.forEach(classObj->{
                    if(!classObj.isInterface()){
                        ServerClassDataStore.setClass(classObj);
                        Class<?>[] interfaceList=classObj.getInterfaces();
                        if(interfaceList!=null){
                            Object instance=null;
                            try {
                                //创建服务实现类实例
                                instance=classObj.newInstance();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            if(instance!=null){
                                for(int i=0;i<interfaceList.length;i++){
                                    Method[] methods=interfaceList[i].getDeclaredMethods();
                                    if(methods!=null) {
                                        Hashtable<String,Method> hashtable=new Hashtable<>(methods.length);
                                        for(int mi=0;mi<methods.length;mi++){
                                            hashtable.put(methods[mi].getName(),methods[mi]);
                                        }
                                        ProviderContainer providerContainer = new ProviderContainer(interfaceList[i].getName(),
                                                instance,hashtable);
                                        services.put(interfaceList[i].getName(),providerContainer);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
        if(services.size()==0){
            System.out.println("services:"+services.size()+";请检查待扫描包配置");
        }
    }
    /**
     * 通过反射执行目标接口的函数
     * @param methodMessage 消费端发送的参数
     * @return
     */
    public static ResultMessage executeMethod(MethodMessage methodMessage) {
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setMsgID(methodMessage.getMsgID());
        Method method = getMethod(methodMessage.getInterfaceName(), methodMessage.getMethod());
        if (method != null) {
            try {
                Object[] methodParamater=getParamaters(methodMessage.getParamater());
                Object resultObj =null;
                if(methodParamater!=null) {
                    Object instance=getServiceInstance(methodMessage.getInterfaceName());
                    resultObj = method.invoke(instance, methodParamater);
                }else {
                    resultMessage.setErrMsg("方法参数转换失败");
                }
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
     * 按照变量名字排序参数
     * @param paramater
     * @return
     */
    private static Object[] getParamaters(List<RpcDataValue> paramater) {
        List<Object> result = null;
        if (paramater.size() > 0) {
            Collections.sort(paramater, new Comparator<RpcDataValue>() {
                @Override
                public int compare(RpcDataValue o1, RpcDataValue o2) {
                    return o1.getArgIndex()-o2.getArgIndex();
                }
            });
            result = new ArrayList<>();
            for (int i = 0; i < paramater.size(); i++) {
                //保证参数顺序和客户端一致
                RpcDataValue rpcDataValue = paramater.get(i);
                Object p= RpcDataHelper.parseServerData(rpcDataValue);
                if (p != null) {
                    result.add(p);
                }else {
                    return null;
                }
            }
        }
        return result.toArray();
    }

    /**
     * 获取需要执行的函数对象
     *
     * @param interfaceName 接口名称
     * @param methodName    函数名称
     * @return
     */
    private static Method getMethod(String interfaceName, String methodName) {
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
