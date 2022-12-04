package com.example.etl.job;

import io.micronaut.runtime.Micronaut;

import java.util.TimeZone;

public class JobApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Micronaut.run(JobApplication.class, args);
    }
}
