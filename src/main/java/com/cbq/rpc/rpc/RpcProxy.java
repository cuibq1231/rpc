package com.cbq.rpc.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import com.cbq.rpc.model.RpcRequest;
import com.cbq.rpc.model.RpcResponse;
import com.cbq.rpc.net.client.NettyClientHandler;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-07-31
 */
public class RpcProxy<T> implements InvocationHandler {
    private Class<T> clazz;

    public RpcProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
//        if (args == null || proxy == null || method == null) {
//            System.out.println("call method proxy exception, clazz=" + clazz);
//            return null;
//        }
        RpcRequest request = new RpcRequest();
        request.setMethodName(method.getName());
        request.setServiceName(method.getDeclaringClass().getName());
        if (args.length > 0) {
            request.setParamTypeList(Stream.of(args).map(Object::getClass).toArray(Class[]::new));
            request.setParamValueList(args);
        }
        NettyClientHandler clientHandler = RpcClient.getClientHandler();
        if (clientHandler == null) {
            System.err.println("get client handle error");
            return null;
        }
        RpcResponse response;
        try {
            response = clientHandler.sendRequest(request);
            if (response != null) {
                return response.getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
