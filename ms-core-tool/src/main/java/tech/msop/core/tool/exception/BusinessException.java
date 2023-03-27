package tech.msop.core.tool.exception;

import tech.msop.core.tool.model.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    @Getter
    private final ResultCode resultCode;

    public BusinessException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILURE;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(ResultCode resultCode, Throwable cause){
        super(cause);
        this.resultCode = resultCode;
    }


    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
