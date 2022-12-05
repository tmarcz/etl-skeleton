package com.example.workers.activities;

import com.example.commons.model.ApplicationSuiteModel;
import com.fasterxml.jackson.databind.JsonNode;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ExecutionActivities {
    // execution steps
    long pipelineStep(long pipelineId);
    ApplicationSuiteModel engineStep(long pipelineId);
    String resourceStep(long id);
    String driverStep(ApplicationSuiteModel applicationSuiteModel);

    //post-execution steps
    String metricsStep(long jobId);
    String billingStep(long jobId);
    String dataControlStep(long jobId);
    String notificationStep(long jobId);
}
