﻿using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Util
{
 public   class JsonHelper
    {
        public static T Deserialize<T>(String jsonData)
        {
            return JsonConvert.DeserializeObject<T>(jsonData);
        }
        public static Object DeserializeObject(String jsonData,Type type)
        {
            return JsonConvert.DeserializeObject(jsonData, type);
        }
        public static Object DeserializeObject(String jsonData)
        {
            return JsonConvert.DeserializeObject(jsonData);
        }
        public static String SerializeObject(Object data)
        {
            return JsonConvert.SerializeObject(data);
        }
    }
}
