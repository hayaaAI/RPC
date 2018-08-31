using System;

namespace Hayaa.RPC.Componet
{
    /// <summary>
    /// 通讯原语
    /// </summary>
    internal class CommunicationPrimitives
    {     
        public static readonly byte[] PROTOCOLHEADERTAG =new byte[2] { (byte)0xaa, (byte)0xbb };
        public const int CLASS =1;
        public const int String =2;
        public const int Boolean =3;
        public const int Byte =4;
        public const int Short =5;
        public const int Integer = 6;
        public const int Long =7;
        public const int Float =8;
        public const int Double =9;
        public const int Decimal =10;
    }
}
