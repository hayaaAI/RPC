using Hayaa.RPC.Service.Protocol;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

namespace Hayaa.RPC.Service.Util
{
    class NetPackageHepler
    {
        public static List<NetPackage> UnPack<T>(T data,String msgID)
        {
            List<NetPackage> result = new List<NetPackage>();
            String jsonStr = JsonHelper.SerializeObject(data);
            Byte[] byteData = System.Text.Encoding.ASCII.GetBytes(jsonStr);//所有语言在网络通信数据全部使用ASCII编码
            int dataIndex = byteData.Length / 900;//留下数据描述空间
            int mod = byteData.Length % 900;
            if (mod != 0)
            {
                dataIndex++;
            }

            bool last = false;
            for (var i = 0; i < dataIndex; i++)
            {
                last = (dataIndex - 1 == i);

                var temp = new NetPackage()
                {
                    MsgID = msgID,
                    MsgIndex = i,
                    MsgData = last ? ((mod == 0) ? new byte[900] : new byte[mod]) : new byte[900]

                };
                Array.Copy(byteData, i * 900, temp.MsgData, 0, temp.MsgData.Length);
                result.Add(temp);
            }
            return result;
        }
        public static T Packing<T>(List<NetPackage> data)
        {
            T result = default(T);
            data.Sort((x, y) => x.MsgIndex.CompareTo(y.MsgIndex));
            List<byte> byteData = new List<byte>();
            data.ForEach(a =>
            {
                byteData.AddRange(a.MsgData.ToList());
            });
            String jsonStr = System.Text.Encoding.ASCII.GetString(byteData.ToArray());
            result = JsonHelper.DeserializeObject<T>(jsonStr);
            return result;
        }
    }
}
