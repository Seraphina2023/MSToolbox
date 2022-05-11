package com.msop.launch.constant;

/**
 * Consul 常量
 * @author ruozhuliufeng
 */
public interface ConsulConstant {
    /**
     * consul dev 地址
     */
    String CONSUL_HOST = "http://localhost";
    /**
     * consul 端口
     */
    String CONSUL_PORT = "8500";
    /**
     * consul 配置文件
     */
    String CONSUL_CONFIG_FORMAT = "yaml";
    /**
     * consul 延时
     */
    String CONSUL_WATCH_DELAY = "1000";
    /**
     * consul 是否启用
     */
    String CONSUL_WATCH_ENABLED = "true";
}
