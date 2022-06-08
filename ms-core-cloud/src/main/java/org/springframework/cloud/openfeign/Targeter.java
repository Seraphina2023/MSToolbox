package org.springframework.cloud.openfeign;

import feign.Feign;
import feign.Target;
import org.springframework.cloud.openfeign.encoding.FeignContentGzipEncodingAutoConfiguration;

/**
 * Target
 *
 * @author ruozhuliufeng
 */
public interface Targeter {

    /**
     * target
     *
     * @param factory factory
     * @param feign   feign
     * @param context context
     * @param target  target
     * @param <T>     target
     * @return T
     */
    <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContentGzipEncodingAutoConfiguration context,
                 Target.HardCodedTarget<T> target);
}
