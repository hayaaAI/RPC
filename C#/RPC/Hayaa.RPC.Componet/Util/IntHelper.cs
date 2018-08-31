using System;
using System.Collections.Generic;
using System.Text;

namespace Hayaa.RPC.Service.Util
{
    internal class IntHelper
    {
        public static int ByteArrayToInt(byte[] val, Boolean isBig=true)
        {
            if (isBig)
            {
                byte[] arr = new byte[] { val[3], val[2], val[1], val[0] };
                return BitConverter.ToInt32(arr, 0);

            }
            else
            {
                return BitConverter.ToInt32(val, 0);
            }
        }
        public static byte[] IntToByteArray(int val, Boolean isBig = true)
        {
            if (isBig)
            {
                var arr = BitConverter.GetBytes(val);
                return new byte[] { arr[3], arr[2], arr[1], arr[0] };
            }
            else
            {
                return BitConverter.GetBytes(val);
            }

        }
    }
}
