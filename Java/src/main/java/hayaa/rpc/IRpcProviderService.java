package hayaa.rpc;

import java.util.Hashtable;

public interface IRpcProviderService {
    ExecuteResult executeMethod(String interfaceName, String method, Hashtable<String,Object> parameter);
}
