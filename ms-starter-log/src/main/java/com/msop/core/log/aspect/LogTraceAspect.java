package com.msop.core.log.aspect;

import com.msop.core.log.utils.LogTraceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 为异步方法添加traceId
 *
 * @author ruozhuliufeng
 */
@Aspect
public class LogTraceAspect {
    @Pointcut("@annotation(org.springframework.scheduling.annotation.Async)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            LogTraceUtil.insert();
            return joinPoint.proceed();
        }finally {
            LogTraceUtil.remove();
        }
    }
}
