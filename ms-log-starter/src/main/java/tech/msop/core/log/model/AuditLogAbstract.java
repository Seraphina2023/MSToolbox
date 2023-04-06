package tech.msop.core.log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tech.msop.core.tool.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 审计日志公共父类
 *
 * @author ruozhuliufeng
 */
@Data
public class AuditLogAbstract implements Serializable {

    protected static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 服务ID
     */
    protected String serviceId;
    /**
     * 服务器 ip
     */
    protected String serverIp;
    /**
     * 服务器名
     */
    protected String serverHost;
    /**
     * 环境
     */
    protected String env;
    /**
     * 操作IP地址
     */
    protected String remoteIp;
    /**
     * 用户代理
     */
    protected String userAgent;
    /**
     * 请求URI
     */
    protected String requestUri;
    /**
     * 操作方式
     */
    protected String method;
    /**
     * 方法类
     */
    protected String methodClass;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 操作提交的数据
     */
    protected String params;
    /**
     * 创建人
     */
    protected String createBy;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    protected Date createTime;
    /**
     * 链路ID
     */
    protected String traceId;
    /**
     * 执行时长
     */
    protected String time;

}
