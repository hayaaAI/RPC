using Hayaa.RPC.Componet.Util;
using System;

namespace Hayaa.RPC.Componet
{
    [Serializable]
    public class RpcDataValue
    {
        /// <summary>
        /// 提供给Json序列化使用
        /// </summary>
        public RpcDataValue()
        {

        }
        /// <summary>
        /// Json序列化无法使用此构造函数
        /// </summary>
        /// <param name="dataType"></param>
        /// <param name="arg"></param>
        public RpcDataValue(Type dataType,Object arg,int argIndex)
        {
            this.DataType = RpcDataHelper.ParseDataType(dataType);
            this.ClassName = dataType.FullName;
            this.ValContainer = JsonHelper.SerializeObject(arg,true);
            this.ArgIndex = argIndex;
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
        /// <summary>
        /// 参数索引号
        /// </summary>
        public int ArgIndex { set; get; }
    }
}
