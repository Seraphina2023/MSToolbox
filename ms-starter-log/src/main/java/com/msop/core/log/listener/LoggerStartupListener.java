package com.msop.core.log.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.msop.core.log.utils.ElkPropsUtil;
import com.msop.core.tool.utils.StringUtil;

public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {
    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext loggerContext) {

    }

    @Override
    public void onReset(LoggerContext loggerContext) {

    }

    @Override
    public void onStop(LoggerContext loggerContext) {

    }

    @Override
    public void onLevelChange(Logger logger, Level level) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        Context context = getContext();
        context.putProperty("ELK_MODE", "FALSE");
        context.putProperty("STDOUT_APPENDER", "STDOUT");
        context.putProperty("INFO_APPENDER", "INFO");
        context.putProperty("ERROR_APPENDER", "ERROR");
        context.putProperty("DESTINATION", "127.0.0.1:9000");
        String destination = ElkPropsUtil.getDestination();
        if (StringUtil.isNotBlank(destination)) {
            context.putProperty("ELK_MODE", "TRUE");
            context.putProperty("STDOUT_APPENDER", "STDOUT_LOGSTASH");
            context.putProperty("INFO_APPENDER", "INFO_LOGSTASH");
            context.putProperty("ERROR_APPENDER", "ERROR_LOGSTASH");
            context.putProperty("DESTINATION", destination);
        }
    }

    @Override
    public boolean isStarted() {
        return false;
    }
}
