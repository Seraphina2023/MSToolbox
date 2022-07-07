package com.msop.core.swagger;

import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.lang.annotation.*;

/**
 * Swagger 配置开关
 *
 * @author ruozhuliufeng
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableSwagger2WebMvc
public @interface EnableSwagger {
}
