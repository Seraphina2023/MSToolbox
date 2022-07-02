package com.msop.core.tenant.annotation;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.msop.core.tenant.dynamic.DsTenantIdProcessor;

import java.lang.annotation.*;

/**
 * 指定租户动态数据源切换
 *
 * @author ruozhuliufeng
 */
@DS(DsTenantIdProcessor.TENANT_ID_KEY)
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantDS {
}
