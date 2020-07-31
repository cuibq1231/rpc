package com.cbq.rpc.rpc;

import java.lang.reflect.Proxy;

import com.cbq.rpc.net.client.NettyClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class RpcClient {
    private int serverPort;
    private String host;

    private static NettyClientHandler clientHandler = new NettyClientHandler();

    public RpcClient(String host, int serverPort) {
        this.host = host;
        this.serverPort = serverPort;
        start();
    }

    public static NettyClientHandler getClientHandler() {
        return clientHandler;
    }

    public <T> T createRpcService(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[] {interfaceClass},
                new RpcProxy<>(interfaceClass)
        );
    }

    //初始化client到server的连接
    private void start() {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host, serverPort);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(clientHandler);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, serverPort).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("连接服务器成功");
            }
            //注册关闭事件
//            channelFuture.channel().closeFuture().addListener(cfl -> {
//                //关闭客户端套接字
//                if (channelFuture.channel() != null) {
//                    channelFuture.channel().close();
//                }
//                //关闭客户端线程组
//                if (eventLoopGroup != null) {
//                    eventLoopGroup.shutdownGracefully();
//                }
//                System.out.println("客户端[" + channelFuture.channel().localAddress().toString() + "]已断开...");
//            });
        } catch (Exception e) {
            System.err.println("连接服务器失败。。。ip：" + host + "post:" + serverPort);
        }
    }

    /**
     * 客户端关闭
     */
    private void close() {

    }


}
