package com.example.drivers.spark;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import java.util.TimeZone;

@OpenAPIDefinition(
        info = @Info(
                title = "Spark Java Driver Application",
                version = "0.1"
        )
)
public class SparkJavaDriverApplication {
    public static void main(String[] args) {
        Micronaut.run(SparkJavaDriverApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
