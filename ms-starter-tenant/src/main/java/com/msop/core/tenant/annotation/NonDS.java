package com.msop.core.tenant.annotation;

import java.lang.annotation.*;

/**
 * 排除租户数据源自动切换
 *
 * @author ruozhuliufeng
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonDS {
}
