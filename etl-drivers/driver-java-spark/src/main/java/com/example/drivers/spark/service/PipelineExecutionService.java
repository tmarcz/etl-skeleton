package com.example.drivers.spark.service;

import com.example.drivers.spark.model.ApplicationSuiteModel;

public interface PipelineExecutionService {
    String run(ApplicationSuiteModel applicationSuiteModel);
}
