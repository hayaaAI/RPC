using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Util
{
   internal class IntHelper
    {
        public static int ByteArrayToInt(byte[] s)
        {
            return BitConverter.ToInt32(s, 0);
        }
        public static byte[] IntToByteArray(int s)
        {
            return BitConverter.GetBytes(s);
        }
    }
}
