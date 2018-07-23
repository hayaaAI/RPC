package hayaa.rpc.client;

import javassist.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class JavassistHelper {
    public static Object createClass(String interfaceName) {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctInterface =null;
        try {
             ctInterface = classPool.getCtClass(interfaceName);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        if(ctInterface==null) return null;
        CtClass proxyClass = classPool.makeClass(interfaceName+"Hayaa_ProxyClass");//命名格式：接口+Hayaa_ProxyClass
        proxyClass.addInterface(ctInterface);
        Class interfaceClass = null;
        try {
            interfaceClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(interfaceClass==null) return null;
        Method[] methods = interfaceClass.getMethods();
        for(int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            CtMethod ctMethod=createMethod(interfaceName,proxyClass,method,i);
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
            serviceResult=proxyClass.toClass().newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return serviceResult;
    }

    private static CtMethod createMethod(String interfaceName,CtClass proxyClass,Method method, int i) {
        CtMethod ctMethod=null;
        String methodCode=createMethodCode(interfaceName,method);
        try {
            ctMethod=CtNewMethod.make(methodCode,proxyClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return ctMethod;
    }

    private static String createMethodCode(String interfaceName,Method method) {
        StringBuilder stringBuilder=new StringBuilder();
        Class[] exceptionTypes = method.getExceptionTypes();
        StringBuilder exceptionBuilder = new StringBuilder();
        //组装方法的Exception声明
        if(exceptionTypes.length > 0) exceptionBuilder.append(" throws ");
        for(int i = 0; i < exceptionTypes.length; i++) {
            if(i != exceptionTypes.length - 1) exceptionBuilder.append(exceptionTypes[i].getName()).append(",");
            else exceptionBuilder.append(exceptionTypes[i].getName());
        }
        StringBuilder parameterBuilder = new StringBuilder("(");
        StringBuilder parameterHashMap = new StringBuilder();
        //组装方法的参数列表
        Parameter[] parameters = method.getParameters();
        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterType = parameter.getType().getName();
            parameterBuilder.append(parameterType);
            parameterBuilder.append(" ");
            parameterBuilder.append(parameter.getName());
            if((parameters.length-1)>i){
                parameterBuilder.append(",");
            }
            parameterHashMap.append("paramater.put(\""+parameter.getName()+"\","+parameter.getName()+");");
        }
        parameterBuilder.append(")");
        StringBuilder methodDeclare = new StringBuilder();
        String methodName = method.getName();
        String methodReturnType = method.getReturnType().getName();
        methodDeclare.append("public ");
        methodDeclare.append(methodReturnType);
        methodDeclare.append(" ");
        methodDeclare.append(methodName);
        methodDeclare.append(parameterBuilder);
        methodDeclare.append(exceptionBuilder);
        methodDeclare.append("{");
        methodDeclare.append("HashMap<String, Object> paramater=new HashMap<>();");
        methodDeclare.append(parameterHashMap);
        methodDeclare.append("String resultJson= ServiceMethdoProxy.invoke(\""+interfaceName+"\",\""+method.getName()+"\",paramater);");
        methodDeclare.append(methodReturnType+" result =null;");
        methodDeclare.append("if(resultJson==null) return result;");
        methodDeclare.append("result =JsonHelper.DeserializeComplexObject(str,new TypeReference<"+methodReturnType+">(){});");
        methodDeclare.append("return result");
        methodDeclare.append("}");
        return stringBuilder.toString();
    }
}
