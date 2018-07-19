using Hayaa.BaseModel;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Common.Config
{
    [Serializable]
  internal  class RPCConfig : BaseData, ConfigContent
    {
        public AppSettings AppSettings { set; get; }
        public ConnectionStrings ConnectionStrings { set; get; }
        //服务注册地址
        public String ServiceReg { set; get; }
        //服务发布地址
        public String ServicePublice { set; get; }
        //心跳服务地址
        public String KeepUrl { set; get; }
        //服务端口
        public int Port { set; get; }
        public ProviderConfig ProviderConfiguation { set; get; }
        public ConsumerConfig ConsumerConfiguation { set; get; }
        internal class ProviderConfig
        {
            public String Name { set; get; }
            public List<ServiceConfig> Services { set; get; }
        }
        internal class ConsumerConfig
        {
            public String Name { set; get; }
            public List<ServiceConfig> Services { set; get; }
        }
        internal class ServiceConfig
        {
            public String Name { set; get; }
            public String Group { set; get; }
            public String InterfaceName { set; get; }
        }
    }
}
