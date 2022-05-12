package com.msop.core.launch.constant;

/**
 * Zookeeper 常量
 *
 * @author ruozhuliufeng
 */
public interface ZookeeperConstant {

    /**
     * zookeeper id
     */
    String ZOOOKEEPER_ID = "zk";

    /**
     * zookeeper connect string
     */
    String ZOOKEEPER_CONNECT_STRING = "127.0.0.1:2181";

    /**
     * zookeeper address
     */
    String ZOOKEEPER_ADDRESS = "zookeeper://" + ZOOKEEPER_CONNECT_STRING;
    /**
     * zookeeper root
     */
    String ZOOKEEPER_ROOT = "/ms-services";
}
