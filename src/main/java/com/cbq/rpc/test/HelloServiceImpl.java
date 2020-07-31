package com.cbq.rpc.test;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-04-26
 */
public class HelloServiceImpl implements Hello {
    public String sayHello(String name) {
        return "hello, nihao" + name;
    }

}
