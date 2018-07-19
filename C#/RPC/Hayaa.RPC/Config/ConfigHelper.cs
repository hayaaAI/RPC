using Hayaa.ConfigSeed.Standard;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Common.Config
{
    internal class ConfigHelper : ConfigTool<RPCConfig, RPCRootConfig>
    {
        private static ConfigHelper _instance = new ConfigHelper();
        private ConfigHelper() : base(DefineTable.RPCComponentID)
        {

        }

        internal static ConfigHelper Instance { get => _instance; }
    }
}
