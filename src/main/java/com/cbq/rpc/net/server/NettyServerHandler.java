package com.cbq.rpc.net.server;

import java.lang.reflect.Method;

import com.cbq.rpc.model.RpcRequest;
import com.cbq.rpc.model.RpcResponse;
import com.cbq.rpc.service.RpcServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            RpcRequest rpcRequest = (RpcRequest) msg;
            //TODO 使用线程池处理
            //ctx的write会从当前的handle传播事件， ctx.channel().write()是从tail开始传播
            ctx.writeAndFlush(getRpcResponse(rpcRequest));
        } catch (Exception e) {
            System.err.println("channelRead server error");
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private RpcResponse getRpcResponse(RpcRequest rpcRequest) {
        try {
            if (rpcRequest == null) {
                throw new Exception("deserialize request error");
            }
            Class cls = Class.forName(RpcServer.getClass(rpcRequest.getServiceName()).getName());
            Method m = cls.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypeList());
            Object result = m.invoke(cls.newInstance(), rpcRequest.getParamValueList());
            return RpcResponse.builder().traceId(rpcRequest.getTraceId()).result(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("getRpcResponse server error");
        }
        return null;
    }
}
