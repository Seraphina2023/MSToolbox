package com.msop.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 服务状态
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServiceHealth {
    @JsonProperty("Node")
    private Node node;
    @JsonProperty("Service")
    private Service service;
    @JsonProperty("Checks")
    private List<Check> checks;

    public static ServiceHealthBuilder builder(){
        return new ServiceHealthBuilder();
    }

    @ToString
    public static class ServiceHealthBuilder{

        private Node node;

        private Service service;

        private List<Check> checks;
        @JsonProperty("Node")
        public ServiceHealthBuilder node(final Node node){
            this.node = node;
            return this;
        }
        @JsonProperty("Service")
        public ServiceHealthBuilder service(final Service service){
            this.service = service;
            return this;
        }
        @JsonProperty("Checks")
        public ServiceHealthBuilder checks(final List<Check> checks){
            this.checks = checks;
            return this;
        }

        public ServiceHealth build(){
            return new ServiceHealth(this.node,this.service,this.checks);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node{
        @JsonProperty("Node")
        private String name;
        @JsonProperty("Address")
        private String address;
        @JsonProperty("Meta")
        private Map<String,String> meta;

        public static NodeBuilder builder(){
            return new NodeBuilder();
        }
        @ToString
        public static class NodeBuilder{
            private String name;
            private String address;
            private Map<String,String> meta;
            @JsonProperty("Node")
            public NodeBuilder name(final String name){
                this.name = name;
                return this;
            }
            @JsonProperty("Address")
            public NodeBuilder address(final String address){
                this.address = address;
                return this;
            }
            @JsonProperty("Meta")
            public NodeBuilder meta(final Map<String,String> meta){
                this.meta = meta;
                return this;
            }


            public Node build(){
                return new Node(this.name,this.address,this.meta);
            }
        }
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Service{
        @JsonProperty("ID")
        private String id;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Tags")
        private List<String> tags;
        @JsonProperty("Address")
        private String address;
        @JsonProperty("Meta")
        private Map<String,String> meta;
        @JsonProperty("Port")
        private int port;

        public static ServiceBuilder builder(){
            return new ServiceBuilder();
        }

        @ToString
        public static class ServiceBuilder{

            private String id;
            private String name;
            private List<String> tags;
            private String address;
            private Map<String,String> meta;
            private int port;

            @JsonProperty("ID")
            public ServiceBuilder id(final String id){
                this.id = id;
                return this;
            }
            @JsonProperty("Name")
            public ServiceBuilder name(final String name){
                this.name = name;
                return this;
            }
            @JsonProperty("Tags")
            public ServiceBuilder tags(final List<String> tags){
                this.tags = tags;
                return this;
            }
            @JsonProperty("Address")
            public ServiceBuilder address(final String address){
                this.address = address;
                return this;
            }
            @JsonProperty("Meta")
            public ServiceBuilder meta(final Map<String,String> meta){
                this.meta = meta;
                return this;
            }
            @JsonProperty("Port")
            public ServiceBuilder port(final int port){
                this.port = port;
                return this;
            }

            public Service build(){
                return new Service(this.id,this.name,this.tags,this.address,this.meta,this.port);
            }
        }
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Check{
        @JsonProperty("Node")
        private String node;
        @JsonProperty("CheckID")
        private String checkId;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Status")
        private String status;

        public static CheckBuilder builder(){
            return new CheckBuilder();
        }

        @ToString
        public static class CheckBuilder{
            @JsonProperty("Node")
            private String node;
            @JsonProperty("CheckID")
            private String checkId;
            @JsonProperty("Name")
            private String name;
            @JsonProperty("Status")
            private String status;

            @JsonProperty("CheckID")
            public CheckBuilder checkId(final String checkId){
                this.checkId = checkId;
                return this;
            }
            @JsonProperty("Name")
            public CheckBuilder name(final String name){
                this.name = name;
                return this;
            }
            @JsonProperty("Status")
            public CheckBuilder status(final String status){
                this.status = status;
                return this;
            }
            @JsonProperty("Node")
            public CheckBuilder node(final String node){
                this.node = node;
                return this;
            }

            public Check build(){
                return new Check(this.node,this.checkId,this.name,this.status);
            }
        }
    }
}
