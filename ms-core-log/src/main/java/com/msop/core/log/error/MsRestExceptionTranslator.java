package com.msop.core.log.error;

import com.msop.core.common.exception.BusinessException;
import com.msop.core.common.exception.IdempotencyException;
import com.msop.core.common.exception.SecureException;
import com.msop.core.common.model.CodeEnum;
import com.msop.core.common.model.Result;
import com.msop.core.common.utils.Func;
import com.msop.core.common.utils.UrlUtil;
import com.msop.core.common.utils.WebUtil;
import com.msop.core.log.publisher.AuditErrorLogPublisher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.Servlet;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;

/**
 * 全局异常处理，处理可遇见的异常
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class MsRestExceptionTranslator {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleError(MissingServletRequestParameterException e) {
        log.error("缺少请求参数:{}", e.getMessage());
        String message = String.format("缺少必要的参数：%s", e.getParameterName());
        return Result.failed(CodeEnum.PARAM_MISS, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleError(MethodArgumentTypeMismatchException e) {
        log.error("请求参数格式错误:{}", e.getMessage());
        String message = String.format("请求参数格式有误:%s", e.getName());
        return Result.failed(CodeEnum.PARAM_TYPE_ERROR, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleError(MethodArgumentNotValidException e) {
        log.error("参数验证失败:{}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleError(BindException e) {
        log.error("参数绑定失败:{}", e.getMessage());
        return handleError(e.getBindingResult());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleError(ConstraintViolationException e) {
        log.warn("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s,%s", path, violation.getMessage());
        return Result.failed(CodeEnum.PARAM_VALID_ERROR, message);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleError(NoHandlerFoundException e) {
        log.error("404没找到请求:{}", e.getMessage());
        return Result.failed(CodeEnum.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleError(HttpMessageNotReadableException e) {
        log.error("消息不能读取:{}", e.getMessage());
        return Result.failed(CodeEnum.MSG_NOT_READABLE, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result handleError(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法:{}", e.getMessage());
        return Result.failed(CodeEnum.METHOD_NOT_SUPPORTED, e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result handler(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型:{}", e.getMessage());
        return Result.failed(CodeEnum.MEDIA_TYPE_NOT_SUPPORTED, e.getMessage());
    }

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
        // 发送服务异常事件
        AuditErrorLogPublisher.publishEvent(e, UrlUtil.getPath(Objects.requireNonNull(WebUtil.getRequest()).getRequestURI()));
        return Result.failed(CodeEnum.INTERNAL_SERVER_ERROR, (Func.isEmpty(e.getMessage()) ? CodeEnum.INTERNAL_SERVER_ERROR.getMessage() : e.getMessage()));
    }

    public Result handleError(BindingResult result) {
        FieldError error = result.getFieldError();
        String message = String.format("%s,%s", error.getField(), error.getDefaultMessage());
        return Result.failed(CodeEnum.PARAM_BIND_ERROR, message);
    }
}