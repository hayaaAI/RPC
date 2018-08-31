using System;
using System.Collections.Generic;

namespace Hayaa.RPC.Common.Config
{
    [Serializable]
    public class RPCConfig 
    {       
        /// <summary>
        /// 单次会话超时时间,单位毫秒
        /// 设置数值最大不建议超过1500
        /// </summary>
        public int SessionTimeout { set; get; }
        /// <summary>
        /// 宿主服务器的cpu核心数
        /// </summary>
        public int CpuCore { set; get; }
        //服务注册地址
        public String ServiceRegUrl { set; get; }
        //服务发布地址
        public String ServicePubliceUrl { set; get; }
        //心跳服务地址
        public String KeepUrl { set; get; }
       
        public ProviderConfig ProviderConfiguation { set; get; }
        public ConsumerConfig ConsumerConfiguation { set; get; }


        public class ProviderConfig
        {
            public ProviderConfig()
            {
                this.Name = "defaultService";
                this.Group = "defaultGroup";
                this.Port = 8080;
                this.MessageSize = 1024 * 1024;
            }
            public String Name { set; get; }
            /// <summary>
            /// 服务组
            /// </summary>
            public String Group { set; get; }
            //服务发布端口
            public int Port { set; get; }
            /// <summary>
            /// 消息大小,单位byte
            /// </summary>
            public int MessageSize { set; get; }
            /// <summary>
            /// 此字段对C#平台无效
            /// </summary>
            public string Packages { get; set; }
            /// <summary>
            /// 需要扫描的程序集名称集合
            /// 采用逗号分隔
            /// </summary>
            public string AssemblyNames { get; set; }

        }
        public class ConsumerConfig
        {
            public ConsumerConfig()
            {
                this.MessageSize= 1024 * 1024;
            }
            public String Name { set; get; }
            public List<ServiceConfig> Services { set; get; }
            /// <summary>
            /// 消息大小,单位byte
            /// </summary>
            public int MessageSize { set; get; }
        }
        public class ServiceConfig
        {
          
            public String Name { set; get; }
            public String Group { set; get; }
            public String InterfaceName { set; get; }
            /// <summary>
            /// 此字段仅对net有效
            /// </summary>
            public String AssemblyName { set; get; }
            public String ServerHost { set; get; }
            public int ServerPort { set; get; }
        }
    }
}
