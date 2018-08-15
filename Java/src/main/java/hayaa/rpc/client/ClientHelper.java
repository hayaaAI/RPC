package hayaa.rpc.client;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.NetPackageHepler;
import hayaa.rpc.common.config.RPCConfigHelper;
import hayaa.rpc.common.config.RpcConfig;
import hayaa.rpc.common.protocol.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class ClientHelper {
    private static ClientHelper g_instance = new ClientHelper();

    public static ClientHelper get_instance() {
        return g_instance;
    }

    /**
     * 构建连接池,进行长链接通信
     * key:接口
     * 此种方式适合均衡接口调用，如果有高频函数，则性能有所损失
     */
    private Hashtable<String, Channel> g_ClientPool = null;
    /**
     * 放置某一个接口请求队列过长造成其他接口堆积
     */
    //private volatile ConcurrentLinkedQueue<MethodMessage> g_Methodqueue = null;
    /**
     * msgID作为key，远程返回结果作为value
     */
    private volatile ConcurrentHashMap<String, ResultMessage> g_ResultDic = null;
    private volatile ConcurrentHashMap<String, List<NetPackage>> g_NetData = null;

    /**
     * cpu核心数,按照最小计算能力默认
     */
    private int cpuCoreTotal = 1;

    /**
     * 通信初始化
     */
    public synchronized void init() {
        int serviceTotal = RPCConfigHelper.getConsumerConfig().getServices().size();
        g_ClientPool = new Hashtable<>(serviceTotal);
        // g_Methodqueue = new ConcurrentLinkedQueue();
        g_ResultDic = new ConcurrentHashMap<>(1000);
        g_NetData = new ConcurrentHashMap<>(1000);
        initNetClient(g_ClientPool);
    }

    public ResultMessage GetResult(String msgID) {
        ResultMessage result = null;
        if (g_ResultDic.containsKey(msgID)) {
            result = g_ResultDic.get(msgID);
            g_ResultDic.remove(msgID);
        }
        return result;
    }

    public Boolean enQueue(MethodMessage methodMessage) {
        // g_Methodqueue.add(methodMessage);
        Boolean result=false;
        if (g_ClientPool.containsKey(methodMessage.getInterfaceName())) {
            Channel channel = g_ClientPool.get(methodMessage.getInterfaceName());
            List<NetPackage> netPackages = NetPackageHepler.UnPack(methodMessage, methodMessage.getMsgID());
            if (netPackages != null) {
                result=true;
                for(int i=0;i<netPackages.size();i++){
                    String msg = JsonHelper.SerializeObject(netPackages.get(i));
                    try {
                        channel.writeAndFlush(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        result= false;
                    }
                }
            }
        }
        return result;
    }

    private void enResultQueue(String msg) {
        NetPackage netPackage = null;
        try {
            netPackage = JsonHelper.DeserializeObject(msg, NetPackage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (netPackage != null) {
            if (!g_NetData.containsKey(netPackage.getMsgID())) {
                g_NetData.put(netPackage.getMsgID(), new ArrayList<>(netPackage.getTotal()));
            }
            g_NetData.get(netPackage.getMsgID()).add(netPackage);
            if (g_NetData.get(netPackage.getMsgID()).size() == netPackage.getTotal()) {
                ResultMessage resultMessage = NetPackageHepler.Packing(g_NetData.get(netPackage.getMsgID()), ResultMessage.class);
                if (resultMessage != null) {
                    g_ResultDic.put(resultMessage.getMsgID(), resultMessage);
                }
            }
        }
    }

    /**
     * 初始化netty设置
     *
     * @param cliPool
     */
    private synchronized void initNetClient(Hashtable<String, Channel> cliPool) {
        //worker负责读写数据
        EventLoopGroup worker = new NioEventLoopGroup();
        //辅助启动类
        Bootstrap bootstrap = new Bootstrap();
        //设置线程池
        bootstrap.group(worker);
        //设置socket工厂
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        RpcConfig.ConsumerConfig serverConfig= RPCConfigHelper.getConsumerConfig();
        try {
            //设置管道
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(serverConfig.getMessageSize(),
                            2,4,0,
                            2));
                    pipeline.addLast(new ClientInHandler());
                }
            });
            List<RpcConfig.ServiceConfig> serviceList =serverConfig.getServices();
            List<ChannelFuture> futrueList = new ArrayList<>(serviceList.size());
            serviceList.forEach(s -> {
                ChannelFuture futrue = null;
                try {
                    futrue = bootstrap.connect(new InetSocketAddress(s.getServerHost(), s.getServerPort())).sync();
                    cliPool.put(s.getInterfaceName(), futrue.channel());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (futrue != null) {
                    futrueList.add(futrue);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("netty client quit");
            //优雅的退出，释放NIO线程组
            worker.shutdownGracefully();
        }
    }

    /**
     * netty客户端读取服务器回复
     */
    private class ClientInHandler extends ChannelInboundHandlerAdapter {
        /**
         * 读取服务端的信息
         *
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("client.channelRead");
            ByteBuf resultStr = (ByteBuf) msg;
            byte[] byteData = new byte[resultStr.readableBytes()];
            resultStr.readBytes(byteData);
            resultStr.release();
            enResultQueue(new String(byteData, 0, byteData.length, "US-ASCII"));
        }

        /**
         * channelActive 事件当连接建立的时候会触发
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client.channelActive");
        }
    }
}
