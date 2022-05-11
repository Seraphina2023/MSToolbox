package com.msop.core.secure.annotation;

import java.lang.annotation.*;

/**
 * 权限注解，用于检查权限，规定访问权限
 *
 * @author ruozhuliufeng
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {

    /**
     * Spring EL 表达式
     * 文档地址：<a href="https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#expressions-operators-logical">https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#expressions-operators-logical</a>
     */
    String value();
}
