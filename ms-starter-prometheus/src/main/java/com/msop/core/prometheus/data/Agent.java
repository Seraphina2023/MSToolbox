package com.msop.core.prometheus.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Agent
 *
 * @author ruozhuliufeng
 */
public class Agent {
    @JsonProperty("Config")
    private Config config;

    Agent(final Config config) {
        this.config = config;
    }

    public static AgentBuilder builder() {
        return new AgentBuilder();
    }

    public Config getConfig() {
        return this.config;
    }


    public static class AgentBuilder {
        private Config config;

        AgentBuilder() {
        }

        @JsonProperty("Config")
        public AgentBuilder config(final Config config) {
            this.config = config;
            return this;
        }

        public Agent build() {
            return new Agent(this.config);
        }

        public String toString() {
            return "Agent.AgentBuilder(config=" + this.config + ")";
        }
    }
}
