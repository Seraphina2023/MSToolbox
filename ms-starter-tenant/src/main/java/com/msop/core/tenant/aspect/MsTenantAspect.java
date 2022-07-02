package com.msop.core.tenant.aspect;

import com.msop.core.tenant.MsTenantHolder;
import com.msop.core.tenant.annotation.TenantIgnore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 自定义租户切面
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Aspect
public class MsTenantAspect {

    @Around("@annotation(tenantIgnore)")
    public Object around(ProceedingJoinPoint point, TenantIgnore tenantIgnore) throws Throwable {
        try {
            //开启忽略
            MsTenantHolder.setIgnore(Boolean.TRUE);
            return point.proceed();
        } finally {
            // 关闭忽略
            MsTenantHolder.clear();
        }
    }
}
