package com.example.drivers.spark.service;

import com.example.commons.client.JobPipelineClient;
import com.example.commons.mock.JobPipelineClientMock;
import com.example.commons.model.ApplicationSuiteModel;
import com.example.drivers.spark.util.SparkRunner;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Primary
@Singleton
public class DefaultPipelineExecutionService implements PipelineExecutionService {

    // TODO: tests only
    // @Inject
    private JobPipelineClient pipelineClient = new JobPipelineClientMock();

    @Override
    public String run(ApplicationSuiteModel applicationSuiteModel) {


        var executor = new SparkRunner(applicationSuiteModel, pipelineClient);
        executor.run();

        return "Ok!";
    }
}
