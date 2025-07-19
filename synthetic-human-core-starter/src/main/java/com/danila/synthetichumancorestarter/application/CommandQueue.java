package com.danila.synthetichumancorestarter.application;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommandQueue {

    private final ThreadPoolExecutor delegate;

    public CommandQueue(int poolSize, int capacity) {
        this.delegate =
                new ThreadPoolExecutor(
                        poolSize, poolSize,
                        0L, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(capacity),
                        (r, executor) -> {
                            throw new QueueOverflowException("queue capacity exceeded");
                        });
    }

    public void submit(Runnable task) {
        try {
            delegate.execute(task);
        } catch (RejectedExecutionException ex) {
            throw new QueueOverflowException("queue capacity exceeded");
        }
    }

    public int size() {
        return delegate.getQueue().size();
    }
}
