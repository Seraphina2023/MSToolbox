package com.msop.core.tenant.annotation;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.msop.core.tenant.dynamic.DsTenantIdProcessor;

import java.lang.annotation.*;

/**
 * 指定租户ID动态数据源切换
 *
 * @author ruozhuliufeng
 */
@DS("tenantId")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantParamDS {
}
