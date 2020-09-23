package com.cbq.rpc.zk;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-09-23
 */
public class ZkConfig {
    public static final String ZOOKEEPER_SERVER = "172.16.165.242:2181,172.16.165.242:2182,172.16.165.242:2183";
    public static final int SESSION_TIMEOUT_MS = 6000;
    public static final int CONNECTION_TIMEOUT_MS = 6000;
    public static final int MAX_RETRIES = 3;
    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final String ZK_ROOT_PATH = "/services-rpc";
}
