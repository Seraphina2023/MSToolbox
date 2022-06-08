package com.msop.core.metrics.sentinel;

import com.alibaba.csp.sentinel.metric.extension.MetricExtension;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SentinelMetricsExtension implements MetricExtension {
    public static final String PASS_REQUESTS_TOTAL = "sentinel_pass_requests_total";
    public static final String BLOCK_REQUESTS_TOTAL = "sentinel_block_requests_total";
    public static final String SUCCESS_REQUESTS_TOTAL = "sentinel_success_requests_total";
    public static final String EXCEPTION_REQUESTS_TOTAL = "sentinel_exception_requests_total";
    public static final String REQUESTS_LATENCY_SECONDS = "sentinel_requests_latency_seconds";
    public static final String CURRENT_THREADS = "sentinel_current_threads";
    public static final String DEFAULT_TAT_NAME = "resource";
    private static AtomicLong CURRENT_THREAD_COUNT = new AtomicLong(0L);

    public SentinelMetricsExtension(){}



    @Override
    public void addPass(String resource, int n, Object... objects) {
        Metrics.counter(PASS_REQUESTS_TOTAL,new String[]{DEFAULT_TAT_NAME,resource}).increment((double) n);
    }


    @Override
    public void addBlock(String resourcce, int n, String origin, BlockException ex, Object... objects) {
        Metrics.counter(BLOCK_REQUESTS_TOTAL,new String[]{resourcce,ex.getClass().getSimpleName(),ex.getRuleLimitApp(),origin}).increment((double) n);
    }


    @Override
    public void addSuccess(String resource, int n, Object... objects) {
        Metrics.counter(SUCCESS_REQUESTS_TOTAL,new String[]{DEFAULT_TAT_NAME,resource}).increment((double) n);
    }


    @Override
    public void addException(String resource, int n, Throwable throwable) {
        Metrics.counter(EXCEPTION_REQUESTS_TOTAL,new String[]{DEFAULT_TAT_NAME,resource}).increment((double) n);
    }


    @Override
    public void addRt(String resource, long rt, Object... objects) {
        Metrics.timer(REQUESTS_LATENCY_SECONDS,new String[]{DEFAULT_TAT_NAME,resource}).record(rt, TimeUnit.MICROSECONDS);
    }


    @Override
    public void increaseThreadNum(String resource, Object... objects) {
        Tags tags = Tags.of(DEFAULT_TAT_NAME,resource);
        Metrics.gauge(CURRENT_THREADS,tags,this.CURRENT_THREAD_COUNT,AtomicLong::incrementAndGet);
    }

    @Override
    public void decreaseThreadNum(String resource, Object... objects) {
        Tags tags = Tags.of(DEFAULT_TAT_NAME,resource);
        Metrics.gauge(CURRENT_THREADS,tags,this.CURRENT_THREAD_COUNT,AtomicLong::decrementAndGet);
    }
}
