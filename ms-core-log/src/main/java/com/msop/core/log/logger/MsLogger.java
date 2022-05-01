package com.msop.core.log.logger;

import com.msop.core.log.publisher.AuditUsualLogPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;

/**
 * 日志工具类
 */
@Slf4j
public class MsLogger implements InitializingBean {
    @Value("${spring.application.name}")
    private String serviceId;

    public void info(String id,String data){
        AuditUsualLogPublisher.publishEvent("info",id,data);
    }
    public void debug(String id,String data){
        AuditUsualLogPublisher.publishEvent("debug",id,data);
    }
    public void warn(String id,String data){
        AuditUsualLogPublisher.publishEvent("warn",id,data);
    }
    public void error(String id,String data){
        AuditUsualLogPublisher.publishEvent("error",id,data);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(serviceId+": MsLogger init Success!");
    }
}
