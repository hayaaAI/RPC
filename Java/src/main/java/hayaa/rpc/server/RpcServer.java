package hayaa.rpc.server;

import hayaa.rpc.IRpcProviderService;
import hayaa.rpc.common.config.RPCConfigHelper;
import hayaa.rpc.common.config.RpcConfig;
import hayaa.rpc.common.protocol.RpcDecoder;
import hayaa.rpc.common.protocol.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RpcServer {
    private IRpcProviderService g_service;
    public IRpcProviderService getService(){
        return g_service;
    }
    public  void run(IRpcProviderService service) throws Exception {
        g_service=service;
        //boss线程监听端口，worker线程负责数据读写
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        //辅助启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置线程池
        bootstrap.group(boss, worker);
        //设置socket工厂
        bootstrap.channel(NioServerSocketChannel.class);
        //设置TCP参数
        //1.链接缓冲池的大小（ServerSocketChannel的设置）
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        //维持链接的活跃，清除死链接(SocketChannel的设置)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //关闭延迟发送
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        RpcConfig.ProviderConfig serverConfig= RPCConfigHelper.getProviderConfig();
        try {
            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new RpcEncoder());
                    pipeline.addLast(new RpcDecoder());
                    pipeline.addLast(new NettyOutboundHandler());
                    pipeline.addLast(new NettyInboundHandler(g_service));
                }
            });
            //绑定端口
            ChannelFuture future = bootstrap.bind(serverConfig.getPort()).sync();
            System.out.println("rpc server start ...... ");
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
