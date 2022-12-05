package com.example.workers.activities;

import com.example.commons.client.DefaultDriverClient;
import com.example.commons.client.DefaultEngineClient;
import com.example.commons.client.DefaultInfrastructureClient;
import com.example.commons.client.DefaultJobPipelineClient;
import com.example.commons.model.ApplicationSuiteModel;
import com.example.commons.model.ExecutionStepPipelineModel;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class DefaultExecutionActivities implements ExecutionActivities {

    @Override
    public long pipelineStep(long id) {
        try {
            // simulating long request
            Thread.sleep(1000);
        } catch (Exception ignored) {
        }
        return id;
    }

    @Override
    public ApplicationSuiteModel engineStep(long pipelineId) {
        ApplicationSuiteModel result = DefaultEngineClient.get(pipelineId);
        return result;
    }

    @Override
    public String resourceStep(long id) {
        String pong = DefaultInfrastructureClient.fake(id);
        return pong;
    }

    @Override
    public String driverStep(ApplicationSuiteModel applicationSuiteModel) {
        String res = DefaultDriverClient.run(applicationSuiteModel);
        return res;
    }

    @Override
    public String metricsStep(long jobId) {
        return null;
    }

    @Override
    public String billingStep(long jobId) {
        return null;
    }

    @Override
    public String dataControlStep(long jobId) {
        return null;
    }

    @Override
    public String notificationStep(long jobId) {
        return null;
    }

//    @Override
//    public String end(long id) {
//        return null;
//    }
//
//    @Override
//    public String terminate(long id) {
//        return null;
//    }
//
//    @Override
//    public String ping() {
//        System.out.println("ping...");
//        return "pong";
//    }

}
