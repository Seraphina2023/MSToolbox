package com.msop.core.mybatis.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.msop.core.common.utils.DateUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author ruozhuliufeng
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 创建人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUser;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Date createTime;
    /**
     * 更新人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUser;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Date updateTime;

    /**
     * 业务状态[1:正常]
     */
    private Integer status;
    /**
     * 状态 [0:未删除 1:已删除]
     */
    @TableLogic
    private Integer isDeleted;
}
