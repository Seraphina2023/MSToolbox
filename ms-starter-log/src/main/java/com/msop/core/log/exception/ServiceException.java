package com.msop.core.log.exception;

import com.msop.core.tool.model.CodeEnum;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author ruozhuliufeng
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter
    private final CodeEnum code;

    public ServiceException(String message) {
        super(message);
        this.code = CodeEnum.FAILURE;
    }

    public ServiceException(CodeEnum code) {
        super(code.getMessage());
        this.code = code;
    }

    public ServiceException(CodeEnum code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * 提高性能
     *
     * @return Throwable
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}
