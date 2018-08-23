using Hayaa.RPC.Service.Util;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Protocol
{
    [Serializable]
    public class RpcDataValue
    {
        public RpcDataValue(Type dataType,Object arg)
        {
            this.DataType = RpcDataHelper.ParseDataType(dataType);
            this.ClassName = dataType.Name;
            this.ValContainer = JsonHelper.SerializeObject(arg);
        }
        /// <summary>
        /// 1-class 可定义类
        /// 2-string
        /// 3-bool
        /// 4-byte
        /// 5-short
        /// 6-int
        /// 7-long
        /// 8-float
        /// 9-double
        /// 10-decimal
        /// 传输的数据类型
        /// </summary>
        public int DataType { set; get; }
        /// <summary>
        /// 参数json序列化后的存储字段
        /// </summary>
        public String ValContainer { set; get; }
        /// <summary>
        /// 参数的类型
        /// 跨平台传输时只有类型1才有使用意义
        /// </summary>
        public String ClassName { set; get; }
    }
}
