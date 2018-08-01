package hayaa.rpc.server;

import hayaa.common.JsonHelper;
import hayaa.rpc.IRpcProviderService;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;
import hayaa.rpc.common.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class NettyInboundHandler extends ChannelInboundHandlerAdapter {
    private IRpcProviderService g_service;
    public NettyInboundHandler(IRpcProviderService service){
        g_service=service;
    }

    /**
     * 读取Client发送的信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server.channelRead");
        RpcProtocol netData = (RpcProtocol) msg;
        String str = new String(netData.getContent(),0,netData.getContentLength());
        MethodMessage methodMessage=JsonHelper.DeserializeObject(str,MethodMessage.class);
        ResultMessage resultMessage=g_service.executeMethod(methodMessage);
        str=JsonHelper.SerializeObject(resultMessage);
        RpcProtocol returnData =new RpcProtocol(str.getBytes("US-ASCII"));
        ctx.writeAndFlush(returnData);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server.channelReadComplete");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
