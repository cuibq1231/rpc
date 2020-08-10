package com.cbq.rpc.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.stream.Stream;

import com.cbq.rpc.model.RpcFuture;
import com.cbq.rpc.model.RpcRequest;
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
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        RpcRequest.RpcRequestBuilder request =
                RpcRequest.builder().traceId(UUID.randomUUID().toString()).methodName(method.getName())
                        .serviceName(clazz.getName());
        if (args.length > 0) {
            request.paramTypeList(Stream.of(args).map(Object::getClass).toArray(Class[]::new)).paramValueList(args);
        }
        NettyClientHandler clientHandler = RpcClient.getClientHandler();
        if (clientHandler == null) {
            System.err.println("get client handle error");
            return null;
        }
        RpcFuture future;
        try {
            future = clientHandler.sendRequest(request.build());
            if (future != null && future.isDone()) {
                return future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
