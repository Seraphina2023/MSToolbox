package tech.msop.core.db.annotation;

import java.lang.annotation.*;

/**
 * MS 创建表主键
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface MsTableId {

}
