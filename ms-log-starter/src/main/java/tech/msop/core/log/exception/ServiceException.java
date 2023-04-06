package tech.msop.core.log.exception;

import tech.msop.core.tool.model.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author ruozhuliufeng
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter
    private final ResultCode code;

    public ServiceException(String message) {
        super(message);
        this.code = ResultCode.FAILURE;
    }

    public ServiceException(ResultCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ServiceException(ResultCode code, Throwable cause) {
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
