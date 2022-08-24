package com.msop.core.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UReport 实体类
 *
 * @author ruozhuliufeng
 */
@Data
@TableName("msop_report_file")
public class ReportFileEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件内容
     */
    private byte[] content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否已删除
     */
    @TableLogic
    private Integer isDeleted;
}
