package hayaa.rpc.server;

import hayaa.rpc.IRPCProviderService;
import hayaa.rpc.common.config.RPCConfig;
import hayaa.rpc.common.config.RPCConfigHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MessagerServer {
    private IRPCProviderService g_irpcProviderService;
    public  MessagerServer(IRPCProviderService irpcProviderService){
        g_irpcProviderService=irpcProviderService;
    }
    public void run(){
        RPCConfig.ProviderConfig serverConfig= RPCConfigHelper.getProviderConfig();
        EventLoopGroup group  = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(group)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = null; // (7)
            try {
                f = b.bind(RPCConfigHelper.getRPCConfig().getPort()).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            try {
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            group .shutdownGracefully();
        }
    }
}
