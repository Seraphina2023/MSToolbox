package com.msop.core.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Swagger属性配置
 * @author ruozhuliufeng
 */
@Data
//@ConfigurationProperties("ossp.swagger")
public class SwaggerProperties {
    /**
     * 是否开启swagger
     */
    private Boolean enabled;
    /**
     * 标题
     */
    private String titile = "";
    /**
     * 描述
     */
    private String description = "";
    /**
     * 版本
     */
    private String version = "";
    /**
     * 许可证
     */
    private String license = "";
    /**
     * 许可证url
     */
    private String licenseUrl = "";
    /**
     * 服务条款url
     */
    private String termsOfServiceUrl = "";
    /**
     * 联系人
     */
    private Contact contact = new Contact();
    /**
     * swagger会解析的包路径
     */
    private String basePackage = "";
    /**
     * swagger会解析的url规则
     */
    private List<String> basePath = new ArrayList<>();
    /**
     * 在bastPath基础上需要排除的url规则
     */
    private List<String> excludePath = new ArrayList<>();
    /**
     * 分组文档
     */
    private Map<String,DocketInfo> docket = new LinkedHashMap<>();

    /**
     * host信息
     */
    private String host = "";
    /**
     * 全局参数配置
     */
    private List<GlobalOperationParameter> globalOperationParameters;


    /**
     * 全局参数配置
     */
    @Data
    public static class GlobalOperationParameter{
        /**
         * 参数名
         */
        private String name;

        /**
         * 描述信息
         */
        private String description;
        /**
         * 指定参数类型
         */
        private String modelRef;
        /**
         * 参数放在那个地方：header/query/path/body/form
         */
        private String parameterType;
        /**
         * 参数是否必须传
         */
        private String required;
    }

    @Data
    public static class DocketInfo{
        /**
         * 标题
         */
        private String titile = "";
        /**
         * 描述
         */
        private String description = "";
        /**
         * 版本
         */
        private String version = "";
        /**
         * 许可证
         */
        private String license = "";
        /**
         * 许可证url
         */
        private String licenseUrl = "";
        /**
         * 服务条款url
         */
        private String termsOfServiceUrl = "";
        /**
         * 联系人
         */
        private Contact contact = new Contact();
        /**
         * swagger会解析的包路径
         */
        private String basePackage = "";
        /**
         * swagger会解析的url规则
         */
        private List<String> basePath = new ArrayList<>();
        /**
         * 在bastPath基础上需要排除的url规则
         */
        private List<String> excludePath = new ArrayList<>();

        /**
         * host信息
         */
        private String host = "";
        /**
         * 全局参数配置
         */
        private List<GlobalOperationParameter> globalOperationParameters;
    }

    @Data
    public static class Contact{
        /**
         * 联系人
         */
        private String name = "";
        /**
         * 联系人url
         */
        private String url = "";
        /**
         * 联系人email
         */
        private String email = "";
    }
}
