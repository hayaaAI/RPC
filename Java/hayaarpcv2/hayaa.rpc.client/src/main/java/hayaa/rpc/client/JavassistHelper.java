package hayaa.rpc.client;


import javassist.*;

/**
 * 以源码形式创建接口实现类
 * @author hsieh
 */
public class JavassistHelper {
    /**
     * 创建接口代理对象
     * @param interfaceName 接口全名称
     * @return
     */
    public static Object createClass(String interfaceName) {
        ClassPool classPool = ClassPool.getDefault();
        //classPool.insertClassPath(new ClassClassPath(ServiceMethdoProxy.class));
        CtClass ctInterface =null;
        try {
            ctInterface = classPool.getCtClass(interfaceName);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if(ctInterface==null){
            return null;
        }
        //命名格式：接口+Hayaa_ProxyClass
        CtClass proxyClass = classPool.makeClass(interfaceName+"Hayaa_ProxyClass");
        proxyClass.addInterface(ctInterface);
        CtMethod[] methods = ctInterface.getDeclaredMethods();
        for(int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            CtMethod ctMethod= null;
            try {
                ctMethod = createMethod(classPool,interfaceName,proxyClass,method);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            if(ctMethod!=null) {
                try {
                    proxyClass.addMethod(ctMethod);
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
        Object serviceResult=null;
        try{
            Class temp=proxyClass.toClass();
            serviceResult=temp.newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return serviceResult;
    }

    /**
     * 构建函数
     * @param classPool
     * @param interfaceName 接口名称
     * @param proxyClass 代理对象类
     * @param method 函数
     * @return
     * @throws NotFoundException
     */
    private static CtMethod createMethod(ClassPool classPool,String interfaceName,CtClass proxyClass,CtMethod method) throws NotFoundException {
        CtClass returnType=classPool.get(method.getReturnType().getName());
        CtClass[] paraCtClass=method.getParameterTypes();
        CtMethod ctMethod=new CtMethod(returnType,method.getName(),paraCtClass,proxyClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setModifiers(ctMethod.getModifiers() | Modifier.VARARGS);
        String methodBody=createMethodCode(interfaceName,method,paraCtClass.length);
        try {
            ctMethod.setBody(methodBody);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return ctMethod;
    }

    /**
     * 构建函数主体
     * @param interfaceName 接口名称
     * @param method 函数对象
     * @param varTotal 变量总数
     * @return
     */
    private static String createMethodCode(String interfaceName,CtMethod method,int varTotal) {
        StringBuilder parameterHashMap = new StringBuilder();
        for(int i = 0; i < varTotal; i++) {
            parameterHashMap.append("hayaa.rpc.common.protocol.RpcDataValue rpcDataVal"+i+"=" +
                    "new hayaa.rpc.common.protocol.RpcDataValue($"+(i+1)+","+i+");");
            parameterHashMap.append("paramater.add(rpcDataVal"+i+");");
        }
        StringBuilder methodDeclare = new StringBuilder();
        String methodReturnType = null;
        try {
            methodReturnType = method.getReturnType().getName();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        methodDeclare.append("{");
        methodDeclare.append("java.util.List paramater=new java.util.ArrayList();");
        methodDeclare.append(parameterHashMap);
        methodDeclare.append("Object resultObj= hayaa.rpc.client.ServiceMethdoProxy.invoke(\""
                +interfaceName+"\",\""+method.getName()+"\",paramater,"+methodReturnType+".class);");
        methodDeclare.append(methodReturnType+" result =null;");
        methodDeclare.append("if(resultObj==null){ return result;} else { result =("+methodReturnType+")resultObj;}");
        methodDeclare.append("return result;");
        methodDeclare.append("}");
        return methodDeclare.toString();
    }
}
