package com.msop.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 服务
 *
 * @author ruozhuliufeng
 */
@Getter
@Builder
public class Service {
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Node")
    private String node;
    @JsonProperty("ServiceAddress")
    private String serviceAddress;
    @JsonProperty("ServiceName")
    private String serviceName;
    @JsonProperty("ServiceId")
    private String serviceId;
    @JsonProperty("ServicePort")
    private int servicePort;
    @JsonProperty("NodeMeta")
    private Map<String, String> nodeMeta;
    @JsonProperty("ServiceMeta")
    private Map<String, String> serviceMeta;
    @JsonProperty("ServiceTags")
    private List<String> serviceTags;
}
