package com.example.workers.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ExecutionActivities {
    String start(long id);
    String end(long id);
    String terminate(long id);
    String ping();
}
