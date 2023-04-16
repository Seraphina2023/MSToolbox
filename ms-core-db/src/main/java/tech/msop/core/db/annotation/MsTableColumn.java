package tech.msop.core.db.annotation;

import java.lang.annotation.*;

/**
 * MS 创建表字段
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface MsTableColumn {
    /**
     * 字段名
     */
    String value() default "";

    /**
     * 数据库中是否存在该字段
     */
    boolean exists() default true;

    /**
     * 字段描述
     */
    String description() default "";
}
