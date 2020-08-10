package com.cbq.rpc.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-08-06
 */

public class RpcFuture {

    public RpcFuture() {
        this.countDownLatch = new CountDownLatch(1);
    }

    private CountDownLatch countDownLatch;
    private RpcRequest rpcRequest;
    private RpcResponse rpcResponse;


    public void done(RpcResponse rpcResponse) {
        this.rpcResponse = rpcResponse;
        countDownLatch.countDown();
    }

    public boolean isDone() {
        try {
            return countDownLatch.await(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Object get() {
        if (rpcResponse == null) {
            return null;
        }
        return rpcResponse.getResult();
    }


    public RpcFuture setRpcRequest(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
        return this;
    }

    public RpcRequest getRpcRequest() {
        return rpcRequest;
    }
}
