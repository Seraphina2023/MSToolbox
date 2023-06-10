package tech.msop.core.tool.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Closeable;

/**
 * 锁对象抽象
 */
@AllArgsConstructor
public class MLock implements AutoCloseable{
    @Getter
    private final Object lock;

    private final DistributedLock locker;

    @Override
    public void close() throws Exception {
        locker.unlock(lock);
    }
}
