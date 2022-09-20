package tech.msop.core.cloud.server;

import io.undertow.Undertow;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.undertow.UndertowOptions.ENABLE_HTTP2;

/**
 * Undertow http2 h2c 配置，对serverlet开启
 *
 * @author ruozhuliufeng
 */
@Configuration
@ConditionalOnClass(Undertow.class)
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
public class UndertowHttp2Configuration {

    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowServletWebServerFactoryWebServerFactoryCustomizer(){
        return factory -> factory.addBuilderCustomizers(builder -> builder.setServerOption(ENABLE_HTTP2,true));
    }
}
