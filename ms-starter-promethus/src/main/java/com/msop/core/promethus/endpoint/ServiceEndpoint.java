package com.msop.core.promethus.endpoint;

import com.msop.core.promethus.data.Service;
import com.msop.core.promethus.data.ServiceHealth;
import com.msop.core.promethus.service.RegistrationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 服务端点
 * @author ruozhuliufeng
 */
@RestController
public class ServiceEndpoint {

    private static final String CONSUL_IDX_HEADER = "X-Consul-Index";
    private static final String QUERY_PARAM_WAIT = "wait";
    private static final String QUERY_PARAM_INDEX = "index";
    private static final Pattern WAIT_PATTERN = Pattern.compile("(\\d*)(m|s|ms|h)");

    private final RegistrationService registrationService;

    public ServiceEndpoint(final RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    @GetMapping(
            value = {"/v1/catalog/services"},
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<Map<String,String[]>>> getServiceNames(@RequestParam(name=QUERY_PARAM_WAIT,required = false) String wait,@RequestParam(name = QUERY_PARAM_INDEX,required = false) Long index){
        return this.registrationService.getServiceNames(getWaitMillis(wait),index).map((item)-> createResponseEntity(item.getItem(),item.getChangeIndex()));
    }

    @GetMapping(
            value = {"/v1/catalog/service/{appName}"},
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<List<Service>>> getService(@PathVariable("appName") String appName,@RequestParam(name=QUERY_PARAM_WAIT,required = false) String wait,@RequestParam(name = QUERY_PARAM_INDEX,required = false) Long index) {
        Objects.requireNonNull(appName, "service name can not be null");
        return this.registrationService.getService(appName, getWaitMillis(wait), index).map((item) -> createResponseEntity(item.getItem(), item.getChangeIndex()));
    }
    @GetMapping(
            value = {"/v1/health/service/{appName}"},
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<List<ServiceHealth>>> getServiceHealth(@PathVariable("appName") String appName, @RequestParam(name=QUERY_PARAM_WAIT,required = false) String wait, @RequestParam(name = QUERY_PARAM_INDEX,required = false) Long index) {
        Assert.isTrue(appName != null,"service name can not be null");
        return this.registrationService.getService(appName, getWaitMillis(wait), index).map((item) -> {
            List<ServiceHealth> services = item.getItem().stream().map(registrationService::getServiceHealth).collect(Collectors.toList());
            return createResponseEntity(services, item.getChangeIndex());
        });
    }


    private static <T> ResponseEntity<T> createResponseEntity(T body,long index){
        return new ResponseEntity<>(body,createHeaders(index), HttpStatus.OK);
    }

    private static MultiValueMap<String,String> createHeaders(long index){
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONSUL_IDX_HEADER,String.valueOf(index));
        return headers;
    }

    private static long getWaitMillis(String wait){
        long millis = TimeUnit.MINUTES.toMillis(5L);
        if (wait != null){
            Matcher matcher = WAIT_PATTERN.matcher(wait);
            if (!matcher.matches()){
                throw new IllegalArgumentException("Invalid wait pattern");
            }
            long value = Long.parseLong(matcher.group(1));
            TimeUnit timeUnit = parseTimeUnit(matcher.group(2));
            millis = timeUnit.toMillis(value);
        }
        return millis + (long) ThreadLocalRandom.current().nextInt((int)millis / 16 + 1);
    }

    private static TimeUnit parseTimeUnit(String unit){
        switch (unit){
            case "h":
                return TimeUnit.HOURS;
            case "m":
                return TimeUnit.MINUTES;
            case "s":
                return TimeUnit.SECONDS;
            case "ms":
                return TimeUnit.MILLISECONDS;
            default:
                throw new IllegalArgumentException("No Valid Time Unit!");
        }
    }
}
