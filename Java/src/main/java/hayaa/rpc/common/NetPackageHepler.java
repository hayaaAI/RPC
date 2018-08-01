package hayaa.rpc.common;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.NetPackage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NetPackageHepler {
    public static <T> List<NetPackage> UnPack(T data,String msgID) {
        List<NetPackage> result = new ArrayList<>();
        String jsonStr = JsonHelper.SerializeObject(data);
        byte[] byteData = null;
        try {
            //所有语言在网络通信数据全部使用ASCII编码
            byteData = jsonStr.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //留下数据描述空间
        int dataIndex = byteData.length / 900;
        int mod = byteData.length % 900;
        if (mod != 0) {
            dataIndex++;
        }
        Boolean last = false;
        for (int i = 0; i < dataIndex; i++) {
            last = (dataIndex - 1 == i);
            NetPackage temp = new NetPackage();
            temp.setMsgID(msgID);
            temp.setMsgIndex(i);
            temp.setTotal(dataIndex);
            temp.setMsgData(last ? ((mod == 0) ? new byte[900] : new byte[mod]) : new byte[900]);
            System.arraycopy(byteData,i*900,temp.getMsgData(),0,temp.getMsgData().length);
            result.add(temp);
        }
        return result;
    }
    public static <T> T Packing(List<NetPackage> data,Class<T> classType)
    {
        T result = null;
        Collections.sort(data, new Comparator<NetPackage>() {
            /**
             * 排序不会出现相等的情况
             * @param x
             * @param y
             * @return
             */
            @Override
            public int compare(NetPackage x, NetPackage y) {
                if(x.getMsgIndex()>y.getMsgIndex()){
                    return  1;
                }
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
        result = JsonHelper.DeserializeObject(jsonStr,classType);
        return result;
    }
}
