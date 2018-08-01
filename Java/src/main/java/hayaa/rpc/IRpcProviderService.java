package hayaa.rpc;

import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;

public interface IRpcProviderService {
    ResultMessage executeMethod(MethodMessage methodMessage);
}
