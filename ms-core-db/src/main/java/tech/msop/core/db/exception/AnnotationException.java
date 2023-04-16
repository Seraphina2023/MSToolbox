package tech.msop.core.db.exception;

/**
 * 注解异常
 *
 * @author ruozhuliufeng
 */
public class AnnotationException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    public AnnotationException(String message) {
        super(message);
    }
}
