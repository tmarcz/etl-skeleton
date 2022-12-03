package com.example.scheduler.domain;

public enum SchedulerType {
    CRON(0),
    INSTANT(1),
    CALENDAR(2);

    private final int value;

    SchedulerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
