package tech.msop.core.log.error;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import tech.msop.core.log.exception.SecureException;
import tech.msop.core.log.properties.AuditLogProperties;
import tech.msop.core.log.publisher.AuditErrorLogPublisher;
import tech.msop.core.tool.exception.BusinessException;
import tech.msop.core.tool.model.ResultCode;
import tech.msop.core.tool.model.Result;
import tech.msop.core.tool.utils.Func;
import tech.msop.core.tool.utils.UrlUtil;
import tech.msop.core.tool.utils.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.Objects;

/**
 * 未知异常转译和发送，方便监听，对未知异常统一处理。Order 排序优先级低
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Order
@RequiredArgsConstructor
@AutoConfiguration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class MsRestExceptionTranslator {

    private final AuditLogProperties properties;

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handler(BusinessException e) {
        log.error("业务异常", e);
        return Result.failed(e.getResultCode(), e.getMessage());
    }

    @ExceptionHandler(SecureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handler(SecureException e) {
        log.error("认证异常", e);
        return Result.failed(e.getResultCode(), e.getMessage());
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleError(Throwable e) {
        log.error("服务器异常", e);
        if (properties.getErrorLog()) {
            // 发送服务异常事件
            AuditErrorLogPublisher.publishEvent(e, UrlUtil.getPath(Objects.requireNonNull(WebUtil.getRequest()).getRequestURI()));
        }
        return Result.failed(ResultCode.INTERNAL_SERVER_ERROR, (Func.isEmpty(e.getMessage()) ? ResultCode.INTERNAL_SERVER_ERROR.getMessage() : e.getMessage()));
    }

}
