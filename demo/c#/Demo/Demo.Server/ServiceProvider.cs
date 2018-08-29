using demo.interfaceservice;
using System;
using System.Collections.Generic;
using System.Text;

namespace Demo.Server
{
    public class ServiceProvider : IService
    {
        public RpcData DoRpc(Paramater paramater)
        {
            return new RpcData() {
                Title = "服务端接收:" + paramater.Title,
                Val = paramater.Val + 10
            };
        }
    }
}
