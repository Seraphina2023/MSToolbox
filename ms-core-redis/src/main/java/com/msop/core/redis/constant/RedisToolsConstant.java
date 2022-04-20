package com.msop.core.redis.constant;

/**
 * Redis常量工具
 * @author ruozhuliufeng
 * @date 2021-07-19
 */
public class RedisToolsConstant {

    private RedisToolsConstant(){throw new IllegalStateException("Utility class");}

    /**
     * 单机
     */
    public final static int SINGLE = 1;

    /**
     * 集群
     */
    public final static int CLUSTER = 2;
}
