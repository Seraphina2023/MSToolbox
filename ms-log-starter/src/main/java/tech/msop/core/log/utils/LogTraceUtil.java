package tech.msop.core.log.utils;

import tech.msop.core.tool.utils.StringUtil;
import org.jboss.logging.MDC;

/**
 * 日志追踪工具类
 *
 * @author ruozhuliufeng
 */
public class LogTraceUtil {
    private static final String UNIQUE_ID = "traceId";

    /**
     * 获取日志追踪ID格式
     */
    public static String getTraceId() {
        return StringUtil.randomUUID();
    }

    /**
     * 插入traceId
     */
    public static boolean insert() {
        MDC.put(UNIQUE_ID, getTraceId());
        return true;
    }

    /**
     * 移除traceId
     */
    public static boolean remove(){
        MDC.remove(UNIQUE_ID);
        return true;
    }
}
