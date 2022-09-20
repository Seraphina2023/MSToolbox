package tech.msop.core.cloud.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import tech.msop.core.cloud.http.logger.HttpLoggingInterceptor;
import tech.msop.core.cloud.http.logger.OkHttpSlf4jLogger;
import tech.msop.core.tool.ssl.DisableValidationTrustManager;
import tech.msop.core.tool.ssl.TrustAllHostNames;
import tech.msop.core.tool.utils.Charsets;
import tech.msop.core.tool.utils.Holder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Http RestTemplateHeaderInterceptor 配置
 *
 * @author ruozhuliufeng
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(OkHttpClient.class)
@ConditionalOnProperty(value = "ms.http.enabled", matchIfMissing = true)
public class RestTemplateConfiguration {
    private final MsHttpProperties properties;

    /**
     * okhttp3 请求日志拦截器
     *
     * @return HttpLoggingInterceptor
     */
    @Bean
    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpSlf4jLogger());
        interceptor.setLevel(properties.getLevel());
        return interceptor;
    }

    /**
     * okhttp3 连接池配置
     *
     * @return ConnectionPool
     */
    @Bean
    @ConditionalOnMissingBean
    public ConnectionPool httpClientConnectionPool() {
        int maxTotalConnections = properties.getMaxConnections();
        long timeToLive = properties.getTimeToLive();
        TimeUnit ttlUnit = properties.getTimeUnit();
        return new ConnectionPool(maxTotalConnections, timeToLive, ttlUnit);
    }

    /**
     * 配置OkHttpClient
     *
     * @param connectionPool 连接池
     * @param interceptor    拦截器
     * @return OkHttpClient
     */
    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool, HttpLoggingInterceptor interceptor) {
        boolean followRedirects = properties.isFollowRedirects();
        int connectTimeout = properties.getConnectionTimeout();
        return this.createBuilder(properties.isDisableSslValidation())
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .addInterceptor(interceptor)
                .build();
    }

    private OkHttpClient.Builder createBuilder(boolean disableSslValidation) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (disableSslValidation) {
            try {
                X509TrustManager disabledTrustManager = DisableValidationTrustManager.INSTANCE;
                TrustManager[] trustManagers = new TrustManager[]{disabledTrustManager};
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagers, Holder.SECURE_RANDOM);
                SSLSocketFactory disabledSslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(disabledSslSocketFactory, disabledTrustManager);
                builder.hostnameVerifier(TrustAllHostNames.INSTANCE);
            } catch (NoSuchAlgorithmException | KeyManagementException exception) {
                log.error("Error setting SSLSocketFactory in OKHttpClient", exception);
            }
        }
        return builder;
    }

    @Bean
    public RestTemplateHeaderInterceptor requestHeaderInterceptor(){
        return new RestTemplateHeaderInterceptor();
    }

    @Configuration(proxyBeanMethods = false)
    @RequiredArgsConstructor
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnProperty(value = "ms.http.rest-template.enable")
    public static class RestTemplateAutoConfiguration{
        private final ApplicationContext context;

        /**
         * 普通的RestTemplate，不透传请求头，一般只做外部http调用
         * @param restTemplateBuilder builder
         * @param okHttpClient client
         * @return restTemplate
         */
        @Bean
        @ConditionalOnMissingBean
        public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,OkHttpClient okHttpClient){
            restTemplateBuilder.requestFactory(()-> new OkHttp3ClientHttpRequestFactory(okHttpClient));
            RestTemplate restTemplate = restTemplateBuilder.build();
            configMessageConverters(context,restTemplate.getMessageConverters());
            return restTemplate;
        }

    }


    @Configuration(proxyBeanMethods = false)
    @RequiredArgsConstructor
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnProperty(value = "ms.http.lb-rest-template.enable")
    public static class LbRestTemplateAutoConfiguration{
        private final ApplicationContext context;

        /**
         * 支持负载均衡的 LbRestTemplate
         * @param restTemplateBuilder builder
         * @param okHttpClient client
         * @return restTemplate
         */
        @Bean
        @ConditionalOnMissingBean
        public LbRestTemplate lbRestTemplate(RestTemplateBuilder restTemplateBuilder,OkHttpClient okHttpClient){
            restTemplateBuilder.requestFactory(()-> new OkHttp3ClientHttpRequestFactory(okHttpClient));
            LbRestTemplate restTemplate = restTemplateBuilder.build(LbRestTemplate.class);
            configMessageConverters(context,restTemplate.getMessageConverters());
            return restTemplate;
        }
    }

    private static void configMessageConverters(ApplicationContext context, List<HttpMessageConverter<?>> converters) {
        converters.removeIf(x->x instanceof StringHttpMessageConverter || x instanceof MappingJackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter(context.getBean(ObjectMapper.class)));
    }

}
