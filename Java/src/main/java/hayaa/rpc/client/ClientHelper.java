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
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private RpcConfig.ConsumerConfig config;

    /**
     * 通信初始化
     */
    public synchronized void init(RpcConfig.ConsumerConfig config) {
        this.config=config;
        int serviceTotal = config.getServices().size();
        g_ClientPool = new Hashtable<>(serviceTotal);
        g_ResultDic = new ConcurrentHashMap<>(1000);
        initNetClient(g_ClientPool);
    }

    public Boolean enQueue(MethodMessage methodMessage) {
        Boolean result = false;
        if (g_ClientPool.containsKey(methodMessage.getInterfaceName())) {
            Channel channel = g_ClientPool.get(methodMessage.getInterfaceName());
            String msg = JsonHelper.SerializeObject(methodMessage);
            RpcProtocol rpcProtocol = new RpcProtocol(msg);
            System.out.println("rpc client send starting");
            ByteBuf echo = Unpooled.directBuffer();
            //写头部标识
            echo.writeBytes(rpcProtocol.getMessageFlag());
            //写数据类型
            echo.writeInt(rpcProtocol.getType());
            //写数据长度
            echo.writeInt(rpcProtocol.getContentLength());
            //写数据
            echo.writeBytes(rpcProtocol.getData());
            try {
                channel.writeAndFlush(echo);
                result = true;
                System.out.println("rpc client send end");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(methodMessage.getInterfaceName() + " client quit");
                resetServiceClient(methodMessage.getInterfaceName());
            }

        }
        return result;
    }

    private void enResultQueue(ResultMessage msg) {
        g_ResultDic.put(msg.getMsgID(), msg);
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
     * 删除超时的会话ID
     * @param msgID
     */
    public void delTimeoutMsgID(String msgID) {
        if (g_ResultDic.containsKey(msgID)) {
            g_ResultDic.remove(msgID);
        }
    }
    private EventLoopGroup worker;
    private Bootstrap bootstrap;

    /**
     * 重新建立一个服务的client与到服务端的长连接
     *
     * @param interfaceName
     */
    private synchronized void resetServiceClient(String interfaceName) {
        RpcConfig.ServiceConfig serviceConfig = this.config.getServices().stream().
                filter(a -> a.getInterfaceName().equals(interfaceName)).collect(Collectors.toList()).get(0);
        if(serviceConfig==null) {
            System.out.println(interfaceName + " can not reset");
            return;}
        System.out.println(interfaceName + " client reset");
        try {
            ChannelFuture futrue = bootstrap.connect(new InetSocketAddress(serviceConfig.getServerHost(), serviceConfig.getServerPort())).sync();
            g_ClientPool.remove(serviceConfig.getInterfaceName());
            g_ClientPool.put(serviceConfig.getInterfaceName(), futrue.channel());
            System.out.println(interfaceName + " client reset success");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(interfaceName + " client reset fail");
        }
    }

    /**
     * 初始化netty设置
     *
     * @param cliPool
     */
    private synchronized void initNetClient(Hashtable<String, Channel> cliPool) {
        //worker负责读写数据
        worker = new NioEventLoopGroup();
        //辅助启动类
        bootstrap = new Bootstrap();
        //设置线程池
        bootstrap.group(worker);
        //设置socket工厂
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
         try {
            //设置管道
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(config.getMessageSize(),
                            2, 4, 0,
                            2));
                    pipeline.addLast(new ClientInHandler());
                }
            });
            List<RpcConfig.ServiceConfig> serviceList = config.getServices();
            serviceList.forEach(s -> {
                ChannelFuture futrue = null;
                try {
                    futrue = bootstrap.connect(new InetSocketAddress(s.getServerHost(), s.getServerPort())).sync();
                    cliPool.put(s.getInterfaceName(), futrue.channel());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            System.out.println("netty client quit");
//            //优雅的退出，释放NIO线程组
//            worker.shutdownGracefully();
//        }
    }



    /**
     * 处理服务端返回的信息
     */
    private class ClientInHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            System.out.println("client read start");
            ByteBuf body = (ByteBuf) msg;
            if (body.readableBytes() <= 0) {
                ctx.fireChannelRead(msg);
            }
            byte dataType = body.readByte();
            int dataLength = body.readInt();
            int dataSize = body.readableBytes();
            byte[] data = new byte[dataSize];
            body.readBytes(data);
            String strMsg = new String(data, Charset.forName("utf-8"));
            System.out.println("client read end");
            ResultMessage resultMessage = JsonHelper.DeserializeObject(strMsg, ResultMessage.class);
            System.out.println("client read data is"+((resultMessage==null)?"null":"data"));
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


    /**
     * 关闭netty客户端并且释放资源
     */
    public void shutdownNetty() {
        this.worker.shutdownGracefully();
    }
}
