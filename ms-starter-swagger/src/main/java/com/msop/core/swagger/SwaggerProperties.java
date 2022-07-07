package com.msop.core.swagger;

import com.msop.core.launch.constant.AppConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Swagger 配置
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**
     * swagger会解析的包路径
     **/
    private List<String> basePackages = new ArrayList<>(Collections.singletonList(AppConstant.BASE_PACKAGES));
    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();
    /**
     * 标题
     **/
    private String title = "微服务开放平台-接口文档系统";
    /**
     * 描述
     **/
    private String description = "微服务开放平台-接口文档系统";
    /**
     * 版本
     **/
    private String version = AppConstant.APPLICATION_VERSION;
    /**
     * 许可证
     **/
    private String license = "Powered By RUOZHULIUFENG";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "https://aixuxi.cn";
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "https://aixuxi.cn";
    /**
     * host信息
     **/
    private String host = "";
    /**
     * 联系人信息
     */
    private Contact contact = new Contact();
    /**
     * 全局统一鉴权配置
     */
    private Authorization authorization = new Authorization();

    @Data
    @NoArgsConstructor
    public static class Contact {
        /**
         * 联系人
         */
        private String name = "ruozhuliufeng";
        /**
         * 联系人url
         */
        private String url = "https://github.com/ruozhuliufeng";
        /**
         * 联系人邮箱地址
         */
        private String email = "ruozhuliufeng@aixuxi.cn";
    }

    @Data
    @NoArgsConstructor
    public static class Authorization {
        /**
         * 鉴权策略ID，需要和SecurityReferences ID保持一致
         */
        private String name = "";
        /**
         * 需要开启鉴权URL的正则表达式
         */
        private String authRegex = "^.*$";
        /**
         * 鉴权作用域列表
         */
        private List<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        /**
         * 鉴权请求头参数列表
         */
        private List<AuthorizationApikey> authorizationApiKeyList = new ArrayList<>();
        /**
         * 接口匹配地址
         */
        private List<String> tokenUrlList = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class AuthorizationScope {
        /**
         * 鉴权策略名称，需要和ApiKey的name保持一致
         */
        private String name = "";
        /**
         * 作用域名称
         */
        private String scope = "";
        /**
         * 作用域描述
         */
        private String description = "";
    }

    @Data
    @NoArgsConstructor
    public static class AuthorizationApikey {
        /**
         * 参数名
         */
        private String name = "";
        /**
         * 参数值
         */
        private String keyName = "";
        /**
         * 参数作用域
         */
        private String passAs = "";
    }
}
