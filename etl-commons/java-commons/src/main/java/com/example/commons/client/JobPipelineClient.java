package com.example.commons.client;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

@Client
public interface JobPipelineClient {
    @Get(uri = "http://localhost:9609/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> sendStatus(@PathVariable long id);

    @Get(uri = "http://localhost:9609/ping", produces = MediaType.APPLICATION_JSON)
//    @Header(name = "Accept", value = "text/plain")
    HttpResponse<String> ping();

    @Post(uri = "http://localhost:9609/status", produces = MediaType.APPLICATION_JSON)
    HttpResponse<ResponseExecutionStepPipelineModel> updateStatus(@Body ExecutionStepPipelineModel model);

}

