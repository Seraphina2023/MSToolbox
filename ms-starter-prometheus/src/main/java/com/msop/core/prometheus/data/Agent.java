package com.msop.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * Agent
 *
 * @author ruozhuliufeng
 */
@Getter
@Builder
public class Agent {
    @JsonProperty("Config")
    private Config config;

}
