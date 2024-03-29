package tech.msop.core.launch.constant;

/**
 * Nacos 常量
 *
 * @author ruozhuliufeng
 */
public interface NacosConstant {
    /**
     * Nacos 服务地址
     */
    String NACOS_ADDR = "127.0.0.1:8848";
    /**
     * Nacos 配置前缀
     */
    String NACOS_CONFIG_PREFIX = "ms";
    /**
     * Nacos 组配置后缀
     */
    String NACOS_GROUP_SUFFIX = "-group";
    /**
     * Nacos 配置文件类型
     */
    String NACOS_CONFIG_FORMAT = "yaml";
    /**
     * Nacos Json 配置文件类型
     */
    String NACOS_CONFIG_JSON_FORMAT = "json";
    /**
     * Nacos 是否刷新
     */
    String NACOS_CONFIG_REFRESH = "true";
    /**
     * Nacos 分组
     */
    String NACOS_CONFIG_GROUP = "DEFAULT_GROUP";
    /**
     * seata 分组
     */
    String NACOS_SETAT_GROUP = "SETAT_GROUP";

    /**
     * 构建服务对应的dataId
     *
     * @param appName 服务名
     * @param profile 环境变量
     * @return dataId
     */
    static String dataId(String appName, String profile) {
        return dataId(appName, profile, NACOS_CONFIG_FORMAT);
    }

    /**
     * 构建服务对应的dataId
     *
     * @param appName 服务名
     * @param profile 环境变量
     * @param format  文件类型
     * @return dataId
     */
    static String dataId(String appName, String profile, String format) {
        return appName + "-" + profile + "." + format;
    }

    /**
     * 服务默认加载的配置
     *
     * @return sharedDataId
     */
    static String sharedDataId() {
        return NACOS_CONFIG_PREFIX + "." + NACOS_CONFIG_FORMAT;
    }

    /**
     * 服务默认加载的配置
     *
     * @param profile 环境变量
     * @return sharedDataId
     */
    static String sharedDataId(String profile) {
        return NACOS_CONFIG_PREFIX + "-" + profile + "." + NACOS_CONFIG_FORMAT;
    }

    /**
     * 服务默认加载的配置
     *
     * @param profile 环境变量
     * @return sharedDataIds
     */
    static String sharedDataIds(String profile) {
        return NACOS_CONFIG_PREFIX + "." + NACOS_CONFIG_FORMAT + "," + NACOS_CONFIG_PREFIX + "-" + profile + NACOS_CONFIG_FORMAT;
    }
}
