package tech.msop.core.log.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import tech.msop.core.log.error.MsErrorAttributes;
import tech.msop.core.log.error.MsErrorController;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

/**
 * 统一异常处理
 *
 * @author ruozhuliufeng
 */
@AutoConfiguration
@AllArgsConstructor
@ConditionalOnWebApplication
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
public class MsErrorMvcAutoConfiguration {

    private final ServerProperties serverProperties;

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class,search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes(){
        return new MsErrorAttributes();
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class,search = SearchStrategy.CURRENT)
    public BasicErrorController basicErrorController(ErrorAttributes errorAttributes){
        return new MsErrorController(errorAttributes,serverProperties.getError());
    }
}
