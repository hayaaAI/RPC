using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Client
{
   public class ServiceMethdoProxy
    {
        public static String Invoke(String interfaceName, String methodName, Dictionary<String, Object> paramater)
        {
            String result = null;
            try
            {

            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);              
            }
            return result;
        }
    }
}
