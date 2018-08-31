using System;

namespace Hayaa.RPC.Componet
{
    [Serializable]
   public class ResultMessage
    {
        public ResultMessage()
        {
            
        }
     
        public String MsgID { set; get; }
        public String InterfaceName { set; get; }
        public String Method { set; get; }
        public String Result { set; get; }
        public String ErrMsg { set; get; }
    }
}
