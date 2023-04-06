package tech.msop.core.log.utils;

import tech.msop.core.tool.constant.StringConstant;

import java.util.Properties;

/**
 * ELK  配置工具
 *
 * @author ruozhuliufeng
 */
public class ElkPropsUtil {

    /**
     * 获取ELK服务器地址
     *
     * @return 服务器地址
     */
    public static String getDestination() {
        Properties props = System.getProperties();
        return props.getProperty("ms.log.elk.destination", StringConstant.EMPTY);
    }
}
