package com.cbq.rpc.rpc;

import com.cbq.rpc.net.server.NettyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-14
 */
public class RpcServer {
    private int port;

    /**
     * 注册端口号
     */
    public void forPort(int port) {
        this.port = port;
        //        RpcServer rr = new RpcServer();
        //        Class clazz = rr.getClass();
        //        Method method = clazz.getDeclaredMethod()
        //        method.invoke()
    }


    /**
     * 启动rpc服务，就是启动netty server，接收client请求
     */
    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); // 连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true); // 不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // 长连接
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new NettyServerHandler());// 添加NettyServerHandler，用来处理Server端接收和处理消息的逻辑
                }
            });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("netty start success，port：" + this.port);
            }
            // 关闭连接
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            System.err.println("netty start error：" + e.getMessage());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
