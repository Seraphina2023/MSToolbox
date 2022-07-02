package com.msop.core.tenant.dynamic;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.NonNull;

import static com.msop.core.launch.constant.AppConstant.BASE_PACKAGES;

/**
 * 租户数据源全局处理器
 */
public class TenantDataSourceGlobalAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private final Advice advice;
    private final Pointcut pointcut;

    public TenantDataSourceGlobalAdvisor(@NonNull TenantDataSourceGlobalInterceptor tenantDataSourceGlobalInterceptor) {
        this.advice = tenantDataSourceGlobalInterceptor;
        this.pointcut = buildPoint();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPoint() {
        AspectJExpressionPointcut cut = new AspectJExpressionPointcut();
        cut.setExpression(
                "(@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)) && " +
                        "(!@annotation(" + BASE_PACKAGES + ".core.tenant.annotation.NonDS) && !@within(" + BASE_PACKAGES + ".core.tenant.annotation.NonDS))"
        );
        return new ComposablePointcut((Pointcut) cut);
    }
}
