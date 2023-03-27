package tech.msop.core.tool.model;

/**
 * 业务代码接口,可实现自定义返回代码
 *
 * @author ruozhuliufeng
 */
public interface IResultCode {
    /**
     * 获取消息
     *
     * @return 消息
     */
    String getMessage();

    /**
     * 获取业务代码
     *
     * @return 业务代码
     */
    int getCode();
}
