package com.msop.core.redis.cache;

import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.ObjectUtil;
import com.msop.core.tool.utils.StringUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.time.Duration;

/**
 * Cache Key
 *
 * @author ruozhuliufeng
 */
public interface ICacheKey {
    /**
     * 获取前缀
     *
     * @return key 前缀
     */
    String getPrefix();

    /**
     * 超时时间
     *
     * @return 超时时间
     */
    @Nullable
    default Duration getExpire() {
        return null;
    }

    default CacheKey getKey(Object... suffix){
        String prefix = this.getPrefix();
        // 拼接参数
        String key;
        if (ObjectUtil.isEmpty(suffix)){
            key = prefix;
        }else {
            key = prefix.concat(StringUtil.join(suffix, StringConstant.COLON));
        }
        Duration expire = this.getExpire();
        return expire == null ? new CacheKey(key) : new CacheKey(key,expire);
    }
}
