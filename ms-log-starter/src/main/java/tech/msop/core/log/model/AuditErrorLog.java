package tech.msop.core.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 审计错误日志
 * @author ruozhuliufeng
 */
@Getter
@Setter
@TableName("audit_error_log")
public class AuditErrorLog extends AuditLogAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 堆栈信息
     */
    private String stackTrace;
    /**
     * 异常名
     */
    private String exceptionName;
    /**
     * 异常消息
     */
    private String message;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 代码行数
     */
    private Integer lineNumber;
}
