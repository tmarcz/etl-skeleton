package com.example.drivers.spark.service;


import com.example.commons.model.ApplicationSuiteModel;

public interface PipelineExecutionService {
    String run(ApplicationSuiteModel applicationSuiteModel);
}
