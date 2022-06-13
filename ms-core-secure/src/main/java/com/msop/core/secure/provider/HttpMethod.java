package com.msop.core.secure.provider;

/**
 * HttpMethod 枚举类
 *
 * @author ruozhuliufeng
 */
public enum HttpMethod {
    /**
     * 请求方法集合
     */
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, ALL;

    /**
     * 匹配枚举
     *
     * @param method 方法名
     * @return 枚举
     */
    public static HttpMethod of(String method) {
        try {
            return valueOf(method);
        } catch (Exception e) {
            return null;
        }
    }
}
