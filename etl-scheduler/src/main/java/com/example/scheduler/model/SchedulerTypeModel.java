package com.example.scheduler.model;

public enum SchedulerTypeModel {
    CRON(0),
    INSTANT(1),
    CALENDAR(2);

    private final int value;

    SchedulerTypeModel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
