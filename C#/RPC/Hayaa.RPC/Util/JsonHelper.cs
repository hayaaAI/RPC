using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Util
{
    class JsonHelper
    {
        public static T DeserializeObject<T>(String jsonData)
        {
            return JsonConvert.DeserializeObject<T>(jsonData);
        }
        public static String SerializeObject(Object data)
        {
            return JsonConvert.SerializeObject(data);
        }
    }
}
