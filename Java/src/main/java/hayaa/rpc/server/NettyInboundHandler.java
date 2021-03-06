package hayaa.rpc.server;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.CommunicationPrimitives;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;
import hayaa.rpc.common.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

class NettyInboundHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取Client发送的信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       // System.out.println("server.channelRead start");
        ByteBuf body = (ByteBuf)msg;
        if(body.readableBytes() <= 0){
            ctx.fireChannelRead(msg);
        }
        int dataLength = body.readInt();
        int dataType = body.readInt();
        int dataSize = body.readableBytes();
        byte [] data = new byte[dataSize];
        body.readBytes(data);
        String strMsg = new String(data,Charset.forName("utf-8"));
        MethodMessage methodMessage =null;
        try {
            methodMessage = JsonHelper.gsonDeserialize(strMsg, MethodMessage.class);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        ResultMessage resultMessage=ProviderFactory.executeMethod(methodMessage);
        strMsg=JsonHelper.SerializeObject(resultMessage);
        RpcProtocol returnData =new RpcProtocol(strMsg);
        ByteBuf echo = Unpooled.directBuffer();
        echo.writeBytes(returnData.getMessageFlag());
        echo.writeInt(returnData.getContentLength());
        echo.writeInt(returnData.getType());
        echo.writeBytes(returnData.getData());
        ctx.writeAndFlush(echo);
      //  System.out.println("server.channelRead end");
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
