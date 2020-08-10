package com.cbq.rpc.service;

import java.util.Map;

import com.cbq.rpc.rpc.NettyServer;
import com.google.common.collect.Maps;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-08-06
 * server注册服务
 */
public class RpcServer extends NettyServer {
    private static Map<String, Class> serverMap = Maps.newHashMap();
    public RpcServer(int port) {
        super(port);
    }

    public static void addService(String interfaceName, Class serviceImpl) {
        serverMap.put(interfaceName, serviceImpl);
    }

    public static Class getClass(String interfaceName) {
        return serverMap.get(interfaceName);
    }
}
