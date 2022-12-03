package com.example.drivers.spark.service;

import com.example.drivers.spark.client.PipelineClient;
import com.example.drivers.spark.model.ApplicationSuiteModel;
import com.example.drivers.spark.util.SparkRunner;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

@Primary
@Singleton
public class DefaultPipelineExecutionService implements PipelineExecutionService {


    private PipelineClient pipelineClient;

    @Override
    public String run(ApplicationSuiteModel applicationSuiteModel) {

        var executor = new SparkRunner(applicationSuiteModel, pipelineClient);
        executor.run();

        return "Ok!";
    }
}
