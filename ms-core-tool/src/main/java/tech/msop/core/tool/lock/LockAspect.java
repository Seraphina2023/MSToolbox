package tech.msop.core.tool.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;
import tech.msop.core.tool.constant.CharConstant;
import tech.msop.core.tool.spel.MsExpressionEvaluator;
import tech.msop.core.tool.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LockAspect implements ApplicationContextAware {
    /**
     * 分布式锁接口
     */
    private final DistributedLock locker;
    /**
     * 表达式处理
     */
    private static final MsExpressionEvaluator EVALUATOR = new MsExpressionEvaluator();
    /**
     * Spring 上下文
     */
    private ApplicationContext applicationContext;


    /**
     * AOP 环切 注解  @RedisLock
     *
     * @param point     切点
     * @param lock 锁注解
     */
    @Around("@annotation(lock)")
    public Object aroundRedisLock(ProceedingJoinPoint point, Lock lock) {
        String lockName = lock.value();
        Assert.hasText(lockName, "@Lock 注解值不能为空");
        // EL表达式
        String lockParam = lock.param();
        // 表达式不为空
        String lockKey;
        if (StringUtil.isNotBlank(lockParam)) {
            String evalAsText = evalLockParam(point, lockParam);
            lockKey = lockName + CharConstant.COLON + evalAsText;
        } else {
            lockKey = lockName;
        }
        LockType lockType = lock.type();
        long waitTime = lock.waitTime();
        long leaseTime = lock.leaseTime();
        TimeUnit timeUnit = lock.timeUnit();
        return locker.lock(lockKey, lockType, waitTime, leaseTime, timeUnit, point::proceed);
    }

    /**
     * 计算参数表达式
     *
     * @param point     切点
     * @param lockParam 参数
     * @return 结果
     */
    private String evalLockParam(ProceedingJoinPoint point, String lockParam) {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();
        Object target = point.getTarget();
        Class<?> targetClass = target.getClass();
        EvaluationContext context = EVALUATOR.createContext(method, args, target, targetClass, applicationContext);
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
        return EVALUATOR.evalAsText(lockParam, elementKey, context);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
