package com.msop.core.boot.config;

import com.msop.core.boot.error.ErrorType;
import com.msop.core.boot.error.ErrorUtil;
import com.msop.core.context.MsContext;
import com.msop.core.context.MsRunnableWrapper;
import com.msop.core.launch.properties.MsProperties;
import com.msop.core.log.constant.EventConstant;
import com.msop.core.log.event.AuditErrorLogEvent;
import com.msop.core.log.model.AuditErrorLog;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ErrorHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步处理
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
@AllArgsConstructor
public class MsExecutorConfiguration extends AsyncConfigurerSupport {

    private final MsContext msContext;
    private final MsProperties msProperties;
    private final ApplicationEventPublisher publisher;

    @Bean
    public TaskExecutorCustomizer taskExecutorCustomizer() {
        return taskExecutor -> {
            taskExecutor.setThreadNamePrefix("async-task-");
            taskExecutor.setTaskDecorator(MsRunnableWrapper::new);
            taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        };
    }

    @Bean
    public TaskSchedulerCustomizer taskSchedulerCustomizer() {
        return taskExecutor -> {
            taskExecutor.setThreadNamePrefix("async-scheduler");
            taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            taskExecutor.setErrorHandler(new MsErrorHandler(msContext, msProperties, publisher));
        };
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MsAsyncUncaughtExceptionHandler(msContext, msProperties, publisher);
    }

    @RequiredArgsConstructor
    private static class MsAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
        private final MsContext msContext;
        private final MsProperties msProperties;
        private final ApplicationEventPublisher publisher;

        @Override
        public void handleUncaughtException(@NonNull Throwable throwable, @NonNull Method method, @NonNull Object... objects) {
            log.error("Unexpected error occurred in async method: {}", method.getName(), throwable);
            AuditErrorLog errorLog = new AuditErrorLog();
            // 服务信息、环境、异常类型
            errorLog.setParams(ErrorType.ASYNC.getType());
//            errorLog.setEnv(msProperties.getEnv());
            errorLog.setServiceId(msProperties.getName());
            errorLog.setRequestUri(msContext.getRequestId());
            // 堆栈信息
            ErrorUtil.initErrorInfo(throwable, errorLog);
            Map<String, Object> event = new HashMap<>(16);
            event.put(EventConstant.EVENT_LOG, errorLog);
            publisher.publishEvent(new AuditErrorLogEvent(event));
        }
    }

    @RequiredArgsConstructor
    private static class MsErrorHandler implements ErrorHandler {
        private final MsContext msContext;
        private final MsProperties msProperties;
        private final ApplicationEventPublisher publisher;

        @Override
        public void handleError(@NonNull Throwable error) {
            log.error("Unexpected scheduler exception", error);
            AuditErrorLog errorLog = new AuditErrorLog();
            // 服务信息、环境、异常类型
            errorLog.setParams(ErrorType.ASYNC.getType());
//            errorLog.setEnv(msProperties.getEnv());
            errorLog.setServiceId(msProperties.getName());
            errorLog.setRequestUri(msContext.getRequestId());
            // 堆栈信息
            ErrorUtil.initErrorInfo(error, errorLog);
            Map<String, Object> event = new HashMap<>(16);
            event.put(EventConstant.EVENT_LOG, errorLog);
            publisher.publishEvent(new AuditErrorLogEvent(event));
        }
    }
}

