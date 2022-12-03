package com.example.scheduler.service;

public interface SchedulerService {
    boolean runFirst();
    void runAll();
    void runAllAsync(int threadCount) throws InterruptedException;
}
