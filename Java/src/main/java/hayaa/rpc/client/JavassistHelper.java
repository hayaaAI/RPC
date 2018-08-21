package hayaa.rpc.client;

import javassist.*;

/**
 * 以源码形式创建接口实现类
 */
public class JavassistHelper {
    public static Object createClass(String interfaceName) {
        ClassPool classPool = ClassPool.getDefault();
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

    private static String createMethodCode(String interfaceName,CtMethod method,int varTotal) {
        StringBuilder parameterHashMap = new StringBuilder();
        for(int i = 0; i < varTotal; i++) {
            parameterHashMap.append("paramater.put(\"args"+i+"\",$"+(i+1)+");");
        }
        StringBuilder methodDeclare = new StringBuilder();
        String methodReturnType = null;
        try {
            methodReturnType = method.getReturnType().getName();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        methodDeclare.append("{");
        methodDeclare.append("java.util.Hashtable paramater=new java.util.Hashtable();");
        methodDeclare.append(parameterHashMap);
        methodDeclare.append(" String resultJson= hayaa.rpc.client.ServiceMethdoProxy.invoke(\""+interfaceName+"\",\""+method.getName()+"\",paramater);");
        methodDeclare.append(methodReturnType+" result =new test1.RpcData();");
        methodDeclare.append("if(resultJson==null){ return result;}");
        methodDeclare.append("result =hayaa.common.JsonHelper.gsonDeserialize(resultJson,"+methodReturnType+".class);");
        methodDeclare.append("return result;");
        methodDeclare.append("}");
        return methodDeclare.toString();
    }
}
