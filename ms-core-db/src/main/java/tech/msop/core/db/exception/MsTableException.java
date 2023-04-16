package tech.msop.core.db.exception;

/**
 * 表相关异常
 *
 * @author ruozhuliufeng
 */
public class MsTableException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    public MsTableException(String message) {
        super(message);
    }
}
