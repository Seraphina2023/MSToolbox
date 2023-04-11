package tech.msop.auth.exception;

import lombok.Getter;
import tech.msop.core.tool.model.ResultCode;

public class SecureException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    @Getter
    private final ResultCode resultCode;

    public SecureException(String message) {
        super(message);
        this.resultCode = ResultCode.UN_AUTHORIZED;
    }

    public SecureException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public SecureException(ResultCode resultCode, Throwable cause){
        super(cause);
        this.resultCode = resultCode;
    }


    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
