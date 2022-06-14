package com.msop.core.actuate.handler;

import com.msop.core.actuate.properties.MsHttpCacheProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/**
 * Http Cache服务
 *
 * @author ruozhuliufeng
 */
public class HttpCacheService implements InitializingBean {

    private final MsHttpCacheProperties properties;
    private final CacheManager cacheManager;
    private Cache cache;

    public HttpCacheService(MsHttpCacheProperties properties, CacheManager cacheManager) {
        this.properties = properties;
        this.cacheManager = cacheManager;
    }

    public boolean get(String key){
        Boolean result = cache.get(key,Boolean.class);
        return Boolean.TRUE.equals(result);
    }

    public void set(String key){
        cache.put(key,Boolean.TRUE);
    }

    public void remove(String key){
        cache.evict(key);
    }

    public void clear(){
        cache.clear();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheManager,"cacheManage must not be null!");
        String cacheName = properties.getCacheName();
        this.cache = cacheManager.getCache(cacheName);
        Assert.notNull(this.cache,"HttpCacheCode cacheName: "+ cacheName + "is not config.");
    }
}
