package com.msop.core.context;

import org.springframework.cglib.core.internal.Function;
import org.springframework.lang.Nullable;

/**
 * MS 微服务上下文
 */
public interface MsContext {

    /**
     * 获取请求ID
     *
     * @return 请求ID
     */
    @Nullable
    String getRequestId();

    /**
     * 获取账户ID
     *
     * @return 账户ID
     */
    @Nullable
    String getAccountId();

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    @Nullable
    String getTenantId();

    /**
     * 获取上下文中的数据
     *
     * @param ctxKey 上下文中的key
     * @return 返回数据
     */
    @Nullable
    String get(String ctxKey);

    /**
     * 获取上下文中的数据
     *
     * @param ctxKey   上下文中的key
     * @param function 函数式
     * @param <T>      泛型
     * @return 返回数据
     */
    @Nullable
    <T> T get(String ctxKey, Function<String, T> function);
}
