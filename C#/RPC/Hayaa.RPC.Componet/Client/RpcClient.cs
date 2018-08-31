
namespace Hayaa.RPC.Componet
{
    /// <summary>
    /// rpc客户端主类
    /// </summary>
    public class RpcClient
    {
        private static RPCConfig g_config = null;
        public  void Run(RPCConfig config)
        {
            g_config = config;
            ServiceFactory.initService(config.ConsumerConfiguation.Services);
            ClientHelper.Instance.Init(config.ConsumerConfiguation.Services);
        }
        internal static RPCConfig GetConfig()
        {
            return g_config;
        }
    }
}
