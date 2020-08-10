package com.cbq.rpc.protocol;

import java.util.List;

import com.cbq.rpc.utils.JacksonUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out)
            throws Exception {
        //在可读字节数小于4的情况下，我们并没有做任何处理，假设剩余可读字节数为3，不足以构成1个int。那么父类ByteToMessageDecoder发现这次解码List中的元素没有变化，
        //则会对in中的剩余3个字节进行缓存，等待下1个字节的到来，之后再回到调用ToIntegerDecoder的decode方法。
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        try {
            byteBuf.markReaderIndex();
            int dataLength = byteBuf.readableBytes();
            byte[] data = new byte[dataLength];
            byteBuf.readBytes(data);

            Object obj = JacksonUtils.deserialize(data, genericClass);
            out.add(obj);
        } catch (Exception e) {
            System.err.println("decoder failed!");
            e.printStackTrace();
        }

    }
}
