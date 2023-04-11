package tech.msop.auth.annotation;

import java.lang.annotation.*;

/**
 * 请求的方法参数 MsUser上添加该注解，则注入当前登录人信息
 * 例1：public void test(@LoginUser MsUser user) //只有username 和 roles
 * 例2：public void test(@LoginUser(isFull = true) MsUser user) //能获取SysUser对象的所有信息
 *
 * @author ruozhulifueng
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
    /**
     * 是否查询 MsUser对象所有信息，true则通过rpc接口查询
     */
    boolean isFull() default false;
}
