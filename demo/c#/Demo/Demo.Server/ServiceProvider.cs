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
            Console.WriteLine("客户端数据:{0};长度:{1}", paramater.Title, paramater.Val);
            var result= new RpcData() {
                Title = "服务端接收:" + paramater.Title,
                Val = paramater.Val + 10
            };           
            return result;
        }
    }
}
