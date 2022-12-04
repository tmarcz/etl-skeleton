package com.example.engine.pipeline;

import io.micronaut.runtime.Micronaut;

import java.util.TimeZone;

public class EnginePipelineApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Micronaut.run(EnginePipelineApplication.class, args);
    }
}
