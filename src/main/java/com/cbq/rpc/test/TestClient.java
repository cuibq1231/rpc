package com.cbq.rpc.test;

import com.cbq.rpc.rpc.RpcClient;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClient client = new RpcClient("172.17.156.161", 8100);
        Hello hello = client.createRpcService(Hello.class);
        String result = hello.sayHello("chenchen");
        System.out.println("get result:" + result);

    }
}
