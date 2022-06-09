package org.springframework.cloud.openfeign;

import feign.Feign;
import feign.Target;
import org.springframework.cloud.openfeign.encoding.FeignContentGzipEncodingAutoConfiguration;

/**
 * 添加Ms默认的fallbackFactory
 *
 * @author ruozhuliufeng
 */
@SuppressWarnings("unchecked")
public class MsHystrixTargeter implements Targeter {
    /**
     * target
     *
     * @param factory factory
     * @param feign   feign
     * @param context context
     * @param target  target
     * @return T
     */
    @Override
    public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContentGzipEncodingAutoConfiguration context, Target.HardCodedTarget<T> target) {
        return null;
    }
}
