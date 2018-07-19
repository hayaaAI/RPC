package hayaa.rpc.common.config;

import Hayaa.ConfigSeed.ConfigTool;

class ConfigHelper extends ConfigTool<RPCConfig,RPCRootConfig> {
    public static ConfigHelper getInstance() {
        return _instance;
    }

    private static ConfigHelper _instance = new ConfigHelper();
    private ConfigHelper(){
        super(DefineTable.RPCComponentID,RPCConfig.class,RPCRootConfig.class);
    }
}
