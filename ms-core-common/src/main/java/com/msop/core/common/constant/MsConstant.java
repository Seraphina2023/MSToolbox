package com.msop.core.common.constant;

/**
 * 通用常量
 *
 * @author ruozhuliufeng
 */
public interface MsConstant {

    /**
     * 锁前缀
     */
    String LOCK_KEY_PREFIX = "LOCK_KEY";
    /**
     * 编码
     */
    String UTF_8 = "UTF-8";
    /**
     * contentType
     */
    String CONTENT_TYPE_NAME = "Content-type";

    /**
     * JSON 资源请求
     */
    String CONTENT_TYPE = "application/json;charset=utf-8";

    /**
     * Security 角色前缀
     */
    String SECURITY_ROLE_PREFIX = "ROLE_";

    /**
     * 数据库主键字段名
     */
    String DB_PRIMARY_KEY = "id";

    /**
     * 业务状态[1：正常]
     */
    Integer DB_STATUS_NORMAL = 1;

    /**
     * 删除状态 [0:正常 1:删除]
     */
    Integer DB_NOT_DELETED = 0;
    Integer DB_IS_DELETED = 1;

    /**
     * 用户锁定状态:[0:正常 1:锁定]
     */
    Integer DB_ADMIN_NON_LOCKED = 0;
    Integer DB_ADMIN_LOCKED = 1;

    /**
     * 顶级父节点ID
     */
    Long TOP_PARENT_ID = 0L;

    /**
     * 管理员对应的租户ID
     */
    String ADMIN_TENANT_ID = "000000";

    /**
     * 日志默认状态
     */
    String LOG_NORMAL_TYPE = "1";

    /**
     * 默认为空数据
     */
    String DEFAULT_NULL_MESSAGE = "暂无数据";

    /**
     * 默认成功消息
     */
    String DEFAULT_SUCCESS_MESSAGE = "操作成功";

    /**
     * 默认失败消息
     */
    String DEFAULT_FAILURE_MESSAGE = "操作失败";
    /**
     * 默认未授权消息
     */
    String DEFAULT_UNAUTHORIZED_MESSAGE = "签名认证失败";
    /**
     * 超级管理员用户名
     */
    String ADMIN_USER_NAME = "admin";
}
