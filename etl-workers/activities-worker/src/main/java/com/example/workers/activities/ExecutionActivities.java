package com.example.workers.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ExecutionActivities {
    String start(int id);
    String end(int id);
    String terminate(int id);
    String ping();
}
