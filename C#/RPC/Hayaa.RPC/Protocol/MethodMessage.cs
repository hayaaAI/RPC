using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Protocol
{
    [Serializable]
   public class MethodMessage
    {
        public String MsgID { set; get; }
        public String InterfaceName { set; get; }
        public String Method { set; get; }
        public Dictionary<String, RpcDataValue> Paramater { set; get; }
    }
}
