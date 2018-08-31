using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Componet.Util
{
 public   class JsonHelper
    {
       private static JsonSerializerSettings settings = new JsonSerializerSettings() {
           ContractResolver = new CamelCasePropertyNamesContractResolver(),
           Formatting = Formatting.Indented
    };
        public static T Deserialize<T>(String jsonData, bool small = false)
        {
            if(small)
                return JsonConvert.DeserializeObject<T>(jsonData, settings);
            else
            return JsonConvert.DeserializeObject<T>(jsonData);
        }
        public static Object DeserializeObject(String jsonData,Type type, bool small = false)
        {
            if (small)
                return JsonConvert.DeserializeObject(jsonData, type, settings);
            else
                return JsonConvert.DeserializeObject(jsonData, type);
        }     
        /// <summary>
        /// json序列化,设定small选择驼峰形式
        /// </summary>
        /// <param name="data"></param>
        /// <param name="small">true表示小写驼峰false表示无处理</param>
        /// <returns></returns>
        public static String SerializeObject(Object data,bool small=false)
        {   
            if (small)
                return JsonConvert.SerializeObject(data, settings);
            else
                return JsonConvert.SerializeObject(data);
        }
    }
}
