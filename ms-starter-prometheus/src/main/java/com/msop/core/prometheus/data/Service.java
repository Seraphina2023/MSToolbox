package com.msop.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
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

    public static ServiceBuilder builder() {
        return new ServiceBuilder();
    }

    @NoArgsConstructor
    public static class ServiceBuilder {
        private String address;
        private String node;

        private String serviceAddress;

        private String serviceName;

        private String serviceId;

        private int servicePort;

        private Map<String, String> nodeMeta;

        private Map<String, String> serviceMeta;

        private List<String> serviceTags;

        @JsonProperty("Address")
        public ServiceBuilder address(final String address) {
            this.address = address;
            return this;
        }

        @JsonProperty("Node")
        public ServiceBuilder node(final String node) {
            this.node = node;
            return this;
        }

        @JsonProperty("ServiceAddress")
        public ServiceBuilder serviceAddress(final String serviceAddress) {
            this.serviceAddress = serviceAddress;
            return this;
        }

        @JsonProperty("ServiceName")
        public ServiceBuilder serviceName(final String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        @JsonProperty("ServiceId")
        public ServiceBuilder serviceId(final String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        @JsonProperty("ServicePort")
        public ServiceBuilder servicePort(final int servicePort) {
            this.servicePort = servicePort;
            return this;
        }

        @JsonProperty("NodeMeta")
        public ServiceBuilder nodeMeta(final Map<String, String> nodeMeta) {
            this.nodeMeta = nodeMeta;
            return this;
        }

        @JsonProperty("ServiceMeta")
        public ServiceBuilder serviceMeta(final Map<String, String> serviceMeta) {
            this.serviceMeta = serviceMeta;
            return this;
        }

        @JsonProperty("ServiceTags")
        public ServiceBuilder serviceTags(final List<String> serviceTags) {
            this.serviceTags = serviceTags;
            return this;
        }

        public Service build() {
            return new Service(this.address, this.node, this.serviceAddress, this.serviceName,
                    this.serviceId, this.servicePort, this.nodeMeta, this.serviceMeta, this.serviceTags);
        }

        public String toString() {
            return "Service.ServiceBuilder(address=" + this.address + ",node=" + this.node +
                    ",serviceAddress=" + this.serviceAddress + ",serviceName=" + this.serviceName + ",serviceId=" + this.serviceId +
                    ",servicePort=" + this.servicePort + ",nodeMeta=" + this.nodeMeta + ",serviceMeta=" + this.serviceMeta +
                    ",serviceTags=" + this.serviceTags +")";
        }
    }
}
