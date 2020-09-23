package com.cbq.rpc.zk;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author cuibq <cuibq@kuaishou.com>
 * Created on 2020-09-19
 */
public class ZkClient {
    private CuratorFramework client;
    private String zookeeperServer;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private int baseSleepTimeMs;
    private int maxRetries;

    public ZkClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder().connectString(zookeeperServer).retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs).build();
        client.start();
    }

    public static ZkClient getInstance() {
        ZkClient client = new ZkClient();
        client.setBaseSleepTimeMs(ZkConfig.BASE_SLEEP_TIME_MS);
        client.setConnectionTimeoutMs(ZkConfig.CONNECTION_TIMEOUT_MS);
        client.setMaxRetries(ZkConfig.MAX_RETRIES);
        client.setSessionTimeoutMs(ZkConfig.SESSION_TIMEOUT_MS);
        client.setZookeeperServer(ZkConfig.ZOOKEEPER_SERVER);
        return client;
    }

    public void register(int port) {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String serviceInstance = ZkConfig.ZK_ROOT_PATH + "-" + hostAddress + "-" + port;
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                    .forPath(ZkConfig.ZK_ROOT_PATH + "/" + serviceInstance);
        } catch (Exception e) {
            System.err.println("注册出错");
        }
    }

    public List<String> getChildren(String path) {
        List<String> childrenList = new ArrayList<>();
        try {
            childrenList = client.getChildren().forPath(path);
        } catch (Exception e) {
            System.err.println("获取子节点出错");
        }
        return childrenList;
    }

    public int getChildrenCount(String path) {
        return getChildren(path).size();
    }

    //    public List<String> getInstances() {
    //        return getChildren("/services");
    //    }
    //
    //    public int getInstancesCount() {
    //        return getInstances().size();
    //    }

    public void stop() {
        client.close();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
