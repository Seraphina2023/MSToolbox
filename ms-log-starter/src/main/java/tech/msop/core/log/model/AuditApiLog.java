package tech.msop.core.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * API审计日志
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
@TableName("audit_api_log")
public class AuditApiLog extends AuditLogAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日志类型
     */
    private String type;
    /**
     * 日志标题
     */
    private String title;

}
