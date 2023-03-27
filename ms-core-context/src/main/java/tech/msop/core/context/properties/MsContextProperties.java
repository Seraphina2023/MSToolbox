package tech.msop.core.context.properties;

import tech.msop.core.tool.constant.MsConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hystrix Headers 配置
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
@ConfigurationProperties(MsContextProperties.PREFIX)
public class MsContextProperties {

    /**
     * 配置前缀
     */
    public static final String PREFIX = "ms.context";

    private Headers headers = new Headers();

    @Getter
    @Setter
    public static class Headers {
        /**
         * 请求ID，默认：Ms-RequestId
         */
        private String requestId = "Ms-RequestId";
        /**
         * 用于 聚合层，向调用层传递用户信息的请求头，默认：Ms-AccountId
         */
        private String accountId = "Ms-AccountId";
        /**
         * 用于 聚合层，向调用层传递租户ID 的请求头，默认：Ms-TenantId
         */
        private String tenantId = "Ms-TenantId";
        /**
         * 自定义 RestTemplate 和 Feign透传到下层的Headers名称列表
         */
        private List<String> allowed = Arrays.asList("X-Real-IP", "x-forwarded-for","version","VERSION",
                "authorization", "Authorization", MsConstant.HEADER.toLowerCase(), MsConstant.HEADER);
    }

    /**
     * 获取跨服务的请求头
     *
     * @return 请求头列表
     */
    public List<String> getCrossHeaders() {
        List<String> headerList = new ArrayList<>();
        headerList.add(headers.getRequestId());
        headerList.add(headers.getAccountId());
        headerList.add(headers.getTenantId());
        headerList.addAll(headers.getAllowed());
        return headerList;
    }
}
