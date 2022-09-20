package tech.msop.core.cloud.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import tech.msop.core.cloud.feign.MsFallbackFactory;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * feign 集成 sentinel自动配置
 * 重写 {@link com.alibaba.cloud.sentinel.feign.SentinelFeign} 适配最新API
 *
 * @author ruozhuliufeng
 */
public class MsFeignSentinel {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends Feign.Builder implements ApplicationContextAware {
        private Contract contract = new Contract.Default();
        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            feignContext = this.applicationContext.getBean(FeignContext.class);

        }

        @Override
        public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Feign.Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        @Override
        public Feign build() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                @SneakyThrows
                @Override
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    // 注解取值以避免循环依赖的问题
                    FeignClient  feignClient = AnnotationUtils.findAnnotation(target.type(),FeignClient.class);
                    Class fallback = feignClient.fallback();
                    Class fallbackFactory = feignClient.fallbackFactory();
                    String contextId = feignClient.contextId();
                    if (!StringUtils.hasText(contextId)){
                        contextId = feignClient.name();
                    }
                    Object fallbackInstance;
                    FallbackFactory fallbackFactoryInstance;
                    // 判断fallback 类型
                    if(void.class != fallback){
                        fallbackInstance = getFromContext(contextId,"fallback",fallback,target.type());
                        return new MsSentinelInvocationHandler(target,dispatch,new FallbackFactory.Default<>(fallbackInstance));
                    }
                    if (void.class != fallbackFactory){
                        fallbackFactoryInstance = (FallbackFactory) getFromContext(contextId,"fallbackFactory",fallbackFactory,FallbackFactory.class);
                        return new MsSentinelInvocationHandler(target,dispatch,fallbackFactoryInstance);
                    }
                    // 默认fallbackFactory
                    MsFallbackFactory msFallbackFactory = new MsFallbackFactory(target);
                    return new MsSentinelInvocationHandler(target,dispatch,msFallbackFactory);
                }

                private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
                    Object fallbackInstance = feignContext.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(
                                String.format("No %s instance of type %s found for feign client %s",
                                        type, fallbackType, name)
                        );
                    }
                    if (!targetType.isAssignableFrom(fallbackType)) {
                        throw new IllegalStateException(
                                String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
                                        type, fallbackType, targetType, name)
                        );
                    }
                    return fallbackInstance;
                }
            });
            super.contract(new SentinelContractHolder(contract));
            return super.build();
        }
    }
}
