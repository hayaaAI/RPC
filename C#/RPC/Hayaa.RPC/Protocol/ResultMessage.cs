using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Protocol
{
    [Serializable]
   public class ResultMessage
    {
        public String InterfaceName { set; get; }
        public String Method { set; get; }
        public String Result { set; get; }
        public String ErrMsg { set; get; }
    }
}
