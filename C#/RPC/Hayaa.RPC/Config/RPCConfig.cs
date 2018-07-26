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
        /// <summary>
        /// 单次会话超时时间,单位毫秒
        /// </summary>
        public int SessionTimeout { set; get; }
        /// <summary>
        /// 宿主服务器的cpu核心数
        /// </summary>
        public int CpuCore { set; get; }
        //服务注册地址
        public String ServiceReg { set; get; }
        //服务发布地址
        public String ServicePublice { set; get; }
        //心跳服务地址
        public String KeepUrl { set; get; }
       
        public ProviderConfig ProviderConfiguation { set; get; }
        public ConsumerConfig ConsumerConfiguation { set; get; }
        internal class ProviderConfig
        {
            public String Name { set; get; }
            /// <summary>
            /// 服务组
            /// </summary>
            public String Group { set; get; }
            //服务发布端口
            public int Port { set; get; }

        }
        internal class ConsumerConfig
        {
            public String Name { set; get; }
            public List<ServiceConfig> Services { set; get; }
        }
        internal class ServiceConfig
        {
            public String ServerHost { set; get; }
            public int ServerPort { set; get; }
            public String Name { set; get; }
            public String Group { set; get; }
            public String InterfaceName { set; get; }
            /// <summary>
            /// 此字段仅对net有效
            /// </summary>
            public String AssemblyName { set; get; }
        }
    }
}
