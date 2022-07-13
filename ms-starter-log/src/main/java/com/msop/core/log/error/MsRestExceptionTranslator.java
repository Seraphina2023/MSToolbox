package com.msop.core.log.error;

import com.msop.core.log.properties.AuditLogProperties;
import com.msop.core.secure.exception.SecureException;
import com.msop.core.log.publisher.AuditErrorLogPublisher;
import com.msop.core.tool.exception.BusinessException;
import com.msop.core.tool.model.CodeEnum;
import com.msop.core.tool.model.Result;
import com.msop.core.tool.utils.Func;
import com.msop.core.tool.utils.UrlUtil;
import com.msop.core.tool.utils.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.Servlet;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Set;

/**
 * 未知异常转译和发送，方便监听，对未知异常统一处理。Order 排序优先级低
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Order
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class MsRestExceptionTranslator {

    private final AuditLogProperties properties;

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handler(BusinessException e) {
        log.error("业务异常", e);
        return Result.failed(e.getCodeEnum(), e.getMessage());
    }

    @ExceptionHandler(SecureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handler(SecureException e) {
        log.error("认证异常", e);
        return Result.failed(e.getCodeEnum(), e.getMessage());
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleError(Throwable e) {
        log.error("服务器异常", e);
        if (properties.getErrorLog()) {
            // 发送服务异常事件
            AuditErrorLogPublisher.publishEvent(e, UrlUtil.getPath(Objects.requireNonNull(WebUtil.getRequest()).getRequestURI()));
        }
        return Result.failed(CodeEnum.INTERNAL_SERVER_ERROR, (Func.isEmpty(e.getMessage()) ? CodeEnum.INTERNAL_SERVER_ERROR.getMessage() : e.getMessage()));
    }

}
