package com.example.scheduler.client;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client
public interface JobPipelineClient {
    @Post(uri = "http://localhost:9610/job/start/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<ResponseExecutionStepPipelineModel> start(long id,  ExecutionStepPipelineModel executionStepPipeline);

    @Post(uri = "http://localhost:9610/job/start/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<ResponseExecutionStepPipelineModel> stop(long id,  ExecutionStepPipelineModel executionStepPipeline);

    @Get(uri = "http://localhost:9610/job/ping", produces = MediaType.TEXT_PLAIN)
    @Header(name = "Accept", value = "text/plain")
    HttpResponse<String> ping();
}
