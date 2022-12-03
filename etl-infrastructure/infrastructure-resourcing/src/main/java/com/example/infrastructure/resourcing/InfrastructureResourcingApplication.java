package com.example.infrastructure.resourcing;

import io.micronaut.runtime.Micronaut;

import java.util.TimeZone;

public class InfrastructureResourcingApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Micronaut.run(InfrastructureResourcingApplication.class, args);
    }
}
