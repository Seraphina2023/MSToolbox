package com.msop.core.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基类
 *
 * @author ruozhuliufeng
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 创建用户
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUser;
    /**
     * 修改用户
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUser;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 业务状态
     */
    private Integer status;
    /**
     * 是否已删除
     */
    private Integer isDeleted;
}
