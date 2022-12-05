package com.example.commons.client;

import com.example.commons.model.ApplicationSuiteModel;
import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client
public interface DriverClient {
    @Get(uri = "http://localhost:9660/{id}", produces = MediaType.APPLICATION_JSON)
    HttpResponse<?> sendStatus(@PathVariable long id);

    @Get(uri = "http://localhost:9660/ping", produces = MediaType.APPLICATION_JSON)
//    @Header(name = "Accept", value = "text/plain")
    HttpResponse<String> ping();

    @Post(uri = "http://localhost:9660/run", produces = MediaType.APPLICATION_JSON)
    HttpResponse<String> run(@Body ApplicationSuiteModel model);

}

