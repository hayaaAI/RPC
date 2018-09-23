package hayaa.rpc.server;

import hayaa.common.StringUtil;
import hayaa.rpc.common.config.RPCConfigHelper;
import hayaa.rpc.common.config.RpcConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务运行启动类
 */
public class RpcServer {


    /**
     * rpc服务端启动入口函数
      * @throws Exception
     */
    public void run() throws Exception {
        RpcConfig.ProviderConfig providerConfig= RPCConfigHelper.getProviderConfig();
        if(providerConfig==null){
            System.out.println("配置文件中ProviderConfig节点未配置");
            return;
        }
      String packages=providerConfig.getPackages();
      if(!StringUtil.IsNullOrEmpty(packages)){
          List<String> packageList= Arrays.stream(packages.split(",")).collect(Collectors.toList());
          ProviderFactory.scanServices(packageList);
          initNetty(providerConfig.getMessageSize(),providerConfig.getPort());
      }else {
          System.out.println("配置文件中ProviderConfig节点packages字段未配置");
      }
    }
    /**
     * 初始化服务端netty
     * @throws Exception
     */
    public  void initNetty(int messageSize,int port) throws Exception {
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
        //维持链接的活跃，清除死链接(SocketChannel的设置)
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //关闭延迟发送
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        try {
            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(messageSize,
                            2,4,0,
                            2));
                    pipeline.addLast(new NettyInboundHandler());
                }
            });
            //绑定端口
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("rpc server start listen on:"+port);
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
