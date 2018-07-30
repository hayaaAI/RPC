using Hayaa.RPC.Service.Client;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service
{
   public class AppComponentRoot
    {
        public AppComponentRoot()
        {
            ClientHelper.Instance.Init();
        }
    }
}
