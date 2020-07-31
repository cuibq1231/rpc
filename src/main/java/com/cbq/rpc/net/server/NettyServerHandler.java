package com.cbq.rpc.net.server;

import java.lang.reflect.Method;

import com.cbq.rpc.model.RpcRequest;
import com.cbq.rpc.model.RpcResponse;
import com.cbq.rpc.utils.JacksonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class NettyServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf buf = (ByteBuf) msg;
            //        System.err.println("服务器接收到客户端消息：" + JacksonUtils.deserialize(buf.array(), RpcRequest.class));


            ctx.writeAndFlush(getRpcResponse(buf));
            System.out.println("服务器回复消息：你好，客户端");
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

    private ByteBuf getRpcResponse(ByteBuf request) {
        try {
            //根据request获取response
            byte[] reqByte = new byte[request.readableBytes()];
            request.readBytes(reqByte);
            RpcRequest rpcRequest = JacksonUtils.deserialize(reqByte, RpcRequest.class);
            RpcResponse response = null;
            try {
                if (rpcRequest == null) {
                    throw new Exception("deserialize request error");
                }
                Class cls = Class.forName("com.cbq.rpc.test.HelloServiceImpl");
                Method m = cls.getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypeList());
                Object result = m.invoke(cls.newInstance(), rpcRequest.getParamValueList());
                response = RpcResponse.builder().result(result).build();
            } catch (Exception e) {
                System.err.println(e);
            }
            byte[] responseBytes = JacksonUtils.serialize2Bytes(response);
            ByteBuf pingMessage = Unpooled.buffer();
            pingMessage.writeBytes(responseBytes);

            return pingMessage;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("getRpcResponse server error");
        }
        return null;
    }
}
