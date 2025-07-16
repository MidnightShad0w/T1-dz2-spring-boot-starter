package com.danila.synthetichumancorestarter.application;

import com.danila.synthetichumancorestarter.domain.Command;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CommandQueue {
    private final BlockingQueue<Command> delegate;

    public CommandQueue(int capacity) { this.delegate = new ArrayBlockingQueue<>(capacity); }

    public void enqueue(Command cmd) {
        if (!delegate.offer(cmd)) {
            throw new QueueOverflowException("queue capacity exceeded");
        }
    }
    public Command take() throws InterruptedException { return delegate.take(); }
    public int size() { return delegate.size(); }
}
