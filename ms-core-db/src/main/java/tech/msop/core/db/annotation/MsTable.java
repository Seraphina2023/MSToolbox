package tech.msop.core.db.annotation;

import java.lang.annotation.*;

/**
 * MS 自动创建表
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface MsTable {
    /**
     * 数据库表名
     */
    String value() default "";
}
