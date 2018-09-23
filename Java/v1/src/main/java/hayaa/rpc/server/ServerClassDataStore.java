package hayaa.rpc.server;

import java.util.Hashtable;

public class ServerClassDataStore {
    private static Hashtable<String,Class> classData=new Hashtable<>(100);
    public static void setClass(Class clazz){
        if(!classData.containsKey(clazz.getName())){
            classData.put(clazz.getName(),clazz);
        }
    }
    public static Class getClass(String className){
        if(classData.containsKey(className)) {
            return classData.get(className);
        }
        return null;
    }

}
