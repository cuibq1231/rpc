package com.cbq.rpc.net.client;

import java.util.Map;

import com.cbq.rpc.model.RpcFuture;
import com.cbq.rpc.model.RpcRequest;
import com.cbq.rpc.model.RpcResponse;
import com.google.common.collect.Maps;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static Map<String, RpcFuture> rpcFutureMap = Maps.newConcurrentMap();

    private ChannelHandlerContext ctx;

    /**
     * 发送消息
     */
    @Override
    public void channelActive(ChannelHandlerContext context) {
        this.ctx = context;
    }

    public RpcFuture sendRequest(RpcRequest rpcRequest) throws InterruptedException {
        RpcFuture future = new RpcFuture().setRpcRequest(rpcRequest);
        //        byte[] data = JacksonUtils.serialize2Bytes(rpcRequest);
        //        ByteBuf byteBuf = Unpooled.buffer();
        //        byteBuf.writeBytes(data);
        ChannelFuture channelFuture = ctx.writeAndFlush(rpcRequest).sync();
        if (!channelFuture.isSuccess()) {
            System.err.println("send request failed");
        }
        rpcFutureMap.put(rpcRequest.getTraceId(), future);
        System.out.println("send request!");
        return future;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("client handle exception,");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 接收服务端消息
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object response) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) response;
        if (rpcResponse == null) {
            throw new Exception("get res empty! ");
        }
        RpcFuture future = rpcFutureMap.get(rpcResponse.getTraceId());
        if (future == null) {
            throw new Exception("get future empty! traceId:" + rpcResponse.getTraceId());
        }
        rpcFutureMap.remove(rpcResponse.getTraceId());
        future.done(rpcResponse);
    }
}
