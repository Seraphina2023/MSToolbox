package com.msop.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * 配置
 *
 * @author ruozhuliufeng
 */
@Getter
@Builder
public class Config {
    @JsonProperty("Datacenter")
    private String dataCenter;

}
