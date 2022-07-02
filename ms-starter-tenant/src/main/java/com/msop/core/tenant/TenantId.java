package com.msop.core.tenant;

/**
 * 租户ID生成器
 *
 * @author ruozhuliufeng
 */
public interface TenantId {

    /**
     * 生成自定义租户ID
     *
     * @return 租户ID
     */
    String generate();
}
