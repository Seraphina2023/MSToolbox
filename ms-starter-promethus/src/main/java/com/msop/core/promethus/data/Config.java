package com.msop.core.promethus.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 配置
 *
 * @author ruozhuliufeng
 */
public class Config {
    @JsonProperty("Datacenter")
    private String dataCenter;

    Config(final String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public static ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    public String getDataCenter() {
        return this.dataCenter;
    }

    public static class ConfigBuilder {
        private String dataCenter;

        ConfigBuilder() {
        }

        @JsonProperty("Datacenter")
        public ConfigBuilder dataCenter(final String dataCenter) {
            this.dataCenter = dataCenter;
            return this;
        }

        public Config build() {
            return new Config(this.dataCenter);
        }

        public String toString() {
            return "Config.ConfigBuilder(dataCenter=" + this.dataCenter + ")";
        }
    }
}
