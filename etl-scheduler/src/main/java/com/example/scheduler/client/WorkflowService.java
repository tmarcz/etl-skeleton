package com.example.scheduler.client;

public interface WorkflowService {
    boolean startJob(int id);
    boolean stopJob(int id);
}
