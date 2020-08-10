package com.cbq.rpc.test;

import com.cbq.rpc.service.RpcServer;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class TestService {
    public static void main(String[] args) {
        RpcServer server = new RpcServer(8100);
        //服务注册
        RpcServer.addService(Hello.class.getName(), HelloServiceImpl.class);
        server.start();
    }
}
