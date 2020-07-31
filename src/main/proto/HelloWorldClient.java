package com.cbq.learn.rpc;

import java.util.concurrent.TimeUnit;

import com.cbq.learn.rpc.java.GreeterGrpc;
import com.cbq.learn.rpc.java.GreeterImpl.HelloReply;
import com.cbq.learn.rpc.java.GreeterImpl.HelloRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @Author cuibq <cuibingqi@kuaishou.com>
 * @Description Created on 2019-09-29
 */
public class HelloWorldClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;


    public HelloWorldClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).build();

        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response = blockingStub.sayHello(request);
        System.out.println(response.getMessage());

    }

    public static void main(String[] args) throws InterruptedException {
        HelloWorldClient client = new HelloWorldClient("127.0.0.1", 50051);
        for (int i = 0; i < 5; i++) {
            client.greet("world:" + i);
        }


    }
}
