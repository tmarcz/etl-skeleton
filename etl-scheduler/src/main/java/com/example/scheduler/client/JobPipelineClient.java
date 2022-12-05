package com.example.scheduler.client;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client
public interface JobPipelineClient {
    @Get(uri = "http://localhost:9609/start/{pipelineId}/{schedulerId}/{jobId}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> start(@PathVariable long pipelineId, @PathVariable long schedulerId,@PathVariable long jobId);

    @Get(uri = "http://localhost:9609/stop/{jobId}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> stop(@PathVariable long jobId);

    @Get(uri = "http://localhost:9609/ping")
    HttpResponse<String> ping();
}