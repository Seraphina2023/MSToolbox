package com.msop.core.tenant.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 指定租户表排除
 *
 * @author ruozhuliufeng
 */
@Component
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableExclude {

    String value() default "";
}
