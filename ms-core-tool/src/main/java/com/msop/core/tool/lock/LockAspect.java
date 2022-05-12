package com.msop.core.tool.lock;

import com.msop.core.tool.exception.LockException;
import com.msop.core.tool.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Objects;

/**
 * 分布式锁切面
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Aspect
public class LockAspect {
    @Autowired(required = false)
    private DistributedLock locker;

    /**
     * 用于SpEL表达式解析
     */
    private SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字
     */
    private DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@within(lock) || @annotation(lock)")
    public Object aroundLock(ProceedingJoinPoint point, Lock lock) throws Throwable {
        if (lock == null) {
            // 获取类上的注解
            lock = point.getTarget().getClass().getDeclaredAnnotation(Lock.class);
        }
        String lockKey = lock.key();
        if (locker == null) {
            throw new LockException("分布式锁为空！");
        }
        if (StringUtil.isEmpty(lockKey)) {
            throw new LockException("锁的key为空！");
        }
        if (lockKey.contains("#")) {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            // 获取方法参数值
            Object[] args = point.getArgs();
            lockKey = getValBySpEL(lockKey, methodSignature, args);
        }
        MLock lockObj = null;
        try {
            // 加锁
            if (lock.waitTime() > 0) {
                lockObj = locker.tryLock(lockKey, lock.waitTime(), lock.leaseTime(), lock.unit(), lock.isFair());
            } else {
                lockObj = locker.lock(lockKey, lock.leaseTime(), lock.unit(), lock.isFair());
            }
            if (lockObj != null) {
                return point.proceed();
            } else {
                throw new LockException("锁等待超时!");
            }
        } finally {
            locker.unlock(lockObj);
        }
    }

    /**
     * 解析SpEL表达式
     *
     * @param lockKey         key
     * @param methodSignature 方法
     * @param args            参数
     * @return String
     */
    private String getValBySpEL(String lockKey, MethodSignature methodSignature, Object[] args) {
        // 获取方法形参名数组
        String[] paramNames = discoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = parser.parseExpression(lockKey);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return Objects.requireNonNull(expression.getValue(context)).toString();
        }
        return null;
    }
}
