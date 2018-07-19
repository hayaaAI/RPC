package hayaa.rpc;

import java.util.HashMap;

public interface IRPCProviderService {
    ExecuteResult executeMethod(String interfaceName,String method,HashMap parameter);
}
