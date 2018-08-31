using System;
using System.Collections.Generic;

namespace Hayaa.RPC.Componet
{
    [Serializable]
   public class MethodMessage
    {
        public String MsgID { set; get; }
        public String InterfaceName { set; get; }
        public String Method { set; get; }
        public List<RpcDataValue> Paramater { set; get; }

    }
}
