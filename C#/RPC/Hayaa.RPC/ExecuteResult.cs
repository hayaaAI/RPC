using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service
{
  public  class ExecuteResult
    {
        //执行完成与否
        public Boolean Action { set; get; }
        public String InterfaceName { set; get; }
        public String MethodName { set; get; }
        public String Result { set; get; }
    }
}
