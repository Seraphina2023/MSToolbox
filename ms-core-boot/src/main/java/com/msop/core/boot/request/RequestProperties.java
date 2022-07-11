package com.msop.core.boot.request;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Request 配置类
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("ms.request")
public class RequestProperties {

	/**
	 * 开启xss
	 */
	private Boolean enabled = true;

	/**
	 * 放行url
	 */
	private List<String> skipUrl = new ArrayList<>();

}
