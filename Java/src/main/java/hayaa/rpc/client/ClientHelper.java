package hayaa.rpc.client;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.config.RPCConfigHelper;
import hayaa.rpc.common.config.RpcConfig;
import hayaa.rpc.common.protocol.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hsieh
 */
public class ClientHelper {
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
        g_ResultDic = new ConcurrentHashMap<>(1000);
        initNetClient(g_ClientPool);
    }

    public Boolean enQueue(MethodMessage methodMessage) {
       Boolean result=false;
        if (g_ClientPool.containsKey(methodMessage.getInterfaceName())) {
            Channel channel = g_ClientPool.get(methodMessage.getInterfaceName());
            String msg=JsonHelper.SerializeObject(methodMessage);
            RpcProtocol rpcProtocol=new RpcProtocol(msg);
            System.out.println("rpc client send starting");
            ByteBuf echo = Unpooled.directBuffer();
            echo.writeBytes(rpcProtocol.getMessageFlag());
            echo.writeInt(rpcProtocol.getContentLength());
            echo.writeInt(rpcProtocol.getType());
            echo.writeBytes(rpcProtocol.getData());
            channel.writeAndFlush(echo);
            result=true;
            System.out.println("rpc client send end");
        }
        return result;
    }
    private void enResultQueue(ResultMessage msg) {
        g_ResultDic.put(msg.getMsgID(),msg);
    }
    public ResultMessage GetResult(String msgID) {
        ResultMessage result = null;
        if (g_ResultDic.containsKey(msgID)) {
            result = g_ResultDic.get(msgID);
            g_ResultDic.remove(msgID);
        }
        return result;
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
          //  worker.shutdownGracefully();
        }
    }

    /**
     * 处理服务端返回的信息
     */
    private class ClientInHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            ByteBuf body = (ByteBuf)msg;
            if(body.readableBytes() <= 0){
                ctx.fireChannelRead(msg);
            }
            int dataLength = body.readInt();
            int dataType= body.readInt();
            int dataSize = body.readableBytes();
            byte [] data = new byte[dataSize];
            body.readBytes(data);
            String strMsg = new String(data,Charset.forName("utf-8"));
            ResultMessage resultMessage=JsonHelper.DeserializeObject(strMsg,ResultMessage.class);
            enResultQueue(resultMessage);
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
