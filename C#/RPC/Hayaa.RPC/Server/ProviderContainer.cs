using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;

namespace Hayaa.RPC.Service.Server
{
  public  class ProviderContainer
    {
        public ProviderContainer(String interfaceName, Object serviceInstance, Dictionary<String, MethodInfo> methods)
        {
            this.InterfaceName = interfaceName;
            this.ServiceInstance = serviceInstance;
            this.Methods = methods;
        }
        public String InterfaceName { set; get; }
        public Object ServiceInstance { set; get; }
        public Dictionary<String, MethodInfo> Methods { set; get; }
    }
}
