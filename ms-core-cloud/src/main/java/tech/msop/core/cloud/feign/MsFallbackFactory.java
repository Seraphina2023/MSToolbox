package tech.msop.core.cloud.feign;

import feign.Target;
import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 默认fallback,避免写过多fallback类
 *
 * @param <T> 泛型
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class MsFallbackFactory<T> implements FallbackFactory<T> {

    private final Target<T> target;

    @Override
    @SuppressWarnings("unchecked")
    public T create(Throwable throwable) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new MsFeignFallback<>(targetType,targetName,throwable));
        return (T) enhancer.create();
    }
}
