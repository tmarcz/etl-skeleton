package com.example;

import io.micronaut.runtime.Micronaut;

import java.util.TimeZone;

public class PipelineApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Micronaut.run(PipelineApplication.class, args);
    }
}
