package com.cbq.rpc.protocol;

import com.cbq.rpc.utils.JacksonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {
//    private Class<?> genericClass;
//
//    public RpcEncoder(Class<?> genericClass) {
//        this.genericClass = genericClass;
//    }
//
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf byteBuf) throws Exception {
        byte[] bytes = JacksonUtils.serialize2Bytes(in);
        byteBuf.writeBytes(bytes);
    }
}
