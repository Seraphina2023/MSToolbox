package com.msop.core.cloud.annotation;

import java.lang.annotation.*;

/**
 * Header 版本处理
 *
 * @author ruozhuliufeng
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiVersion {
    /**
     * header 路径中的版本
     *
     * @return 版本号
     */
    String value() default "";
}
