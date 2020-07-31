package com.cbq.rpc.test;

import com.cbq.rpc.rpc.RpcServer;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class TestService {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        server.forPort(8100);
        server.start();
    }
}
