package com.cbq.rpc.net.client;

import com.cbq.rpc.model.RpcRequest;
import com.cbq.rpc.model.RpcResponse;
import com.cbq.rpc.utils.JacksonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
    private RpcResponse rpcResponse;

    private RpcRequest rpcRequest;

    private ChannelHandlerContext ctx;

    public void setRpcRequest(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
    }

    public RpcResponse getRpcResponse() {
        return rpcResponse;
    }

    /**
     * 发送消息
     */
    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        this.ctx = context;
        super.channelActive(context);
    }

    public RpcResponse sendRequest(RpcRequest rpcRequestParam) throws InterruptedException {
        rpcRequest = rpcRequestParam;
        byte[] data = JacksonUtils.serialize2Bytes(rpcRequest);
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(data);
        ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf).sync();
        if (!channelFuture.isSuccess()) {
            System.err.println("send request failed");
        }
        return rpcResponse;
    }

    /**
     * 接收服务端消息
     */
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        rpcResponse = JacksonUtils.deserialize(con, RpcResponse.class);
        System.out.println("客户端收到服务器消息:" + JacksonUtils.serialize(rpcResponse));
    }


}
