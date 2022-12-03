package com.example.scheduler;

import io.micronaut.runtime.Micronaut;
import javafx.application.Application;

import java.sql.DriverManager;
import java.util.TimeZone;

public class SchedulerApplication {
    public static void main(String[] args) throws Exception {
        DriverManager.registerDriver(new org.postgresql.Driver());
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Micronaut.run(SchedulerApplication.class, args);
    }
}
