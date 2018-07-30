package hayaa.rpc.common;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.NetPackage;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NetPackageHepler {
    public static List<NetPackage> UnPack(MethodMessage data) {
        List<NetPackage> result = new ArrayList<>();
        String jsonStr = JsonHelper.SerializeObject(data);
        byte[] byteData = null;
        try {
            byteData = jsonStr.getBytes("US-ASCII");//所有语言在网络通信数据全部使用ASCII编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int dataIndex = byteData.length / 900;//留下数据描述空间
        int mod = byteData.length % 900;
        if (mod != 0) {
            dataIndex++;
        }
        Boolean last = false;
        for (int i = 0; i < dataIndex; i++) {
            last = (dataIndex - 1 == i);
            NetPackage temp = new NetPackage();
            temp.setMsgID(data.getMsgID());
            temp.setMsgIndex(i);
            temp.setMsgData(last ? ((mod == 0) ? new byte[900] : new byte[mod]) : new byte[900]);
            System.arraycopy(byteData,i*900,temp.getMsgData(),0,temp.getMsgData().length);
            result.add(temp);
        }
        return result;
    }
    public static MethodMessage Packing(List<NetPackage> data)
    {
        MethodMessage result = null;
        Collections.sort(data, new Comparator<NetPackage>() {
            /**
             * 排序不会出现相等的情况
             * @param x
             * @param y
             * @return
             */
            @Override
            public int compare(NetPackage x, NetPackage y) {
                if(x.getMsgIndex()>y.getMsgIndex()) return  1;
                return -1;
            }
        });
        int arrTotal=0;
        for(int i=0;i<data.size();i++){
            arrTotal=arrTotal+data.get(i).getMsgData().length;
        }
        byte[] byteData=new byte[arrTotal];

        String jsonStr = null;
        try {
            jsonStr = new String(byteData,0,byteData.length,"US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = JsonHelper.DeserializeObject(jsonStr,MethodMessage.class);
        return result;
    }
}
