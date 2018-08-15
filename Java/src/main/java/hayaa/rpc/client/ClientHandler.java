package hayaa.rpc.client;

import hayaa.common.JsonHelper;
import hayaa.rpc.common.protocol.MethodMessage;
import hayaa.rpc.common.protocol.ResultMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class ClientHandler extends ChannelInboundHandlerAdapter {
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
