using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

namespace Hayaa.NetNio.Service
{
    class EncodingUTF8Helper
    {
        /// <summary>
        /// 转换字节数组为UTF8字符串
        /// </summary>
        /// <param name="arr">数值</param>
        /// <returns></returns>
        public static String ParseBytesToString(byte[] arr)
        {                      
            return Encoding.UTF8.GetString(arr); 
        }
        /// <summary>
        /// 转换字符串为UTF8的字节数组
        /// </summary>
        /// <param name="val">数值</param>
        /// <returns></returns>
        public static byte[] ParseStringToBytes(String val)
        {
           return Encoding.UTF8.GetBytes(val);           
        }
    }
}
