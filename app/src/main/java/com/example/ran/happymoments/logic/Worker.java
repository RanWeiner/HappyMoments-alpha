package com.example.ran.happymoments.logic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Runnable{
    private Thread thread;
    private Runnable task;
    private AtomicBoolean started;
    private CountDownLatch latch;

    public Worker(Runnable task , CountDownLatch latch) {
        thread = new Thread(this);
        started = new AtomicBoolean(false);
        this.task = task;
        this.latch = latch;
    }

    public void start(){
        if (!started.getAndSet(true)) {
            thread.start();
        }
    }

    @Override
    public void run() {
        task.run();
        latch.countDown();
    }
}
