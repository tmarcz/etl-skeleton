package com.example.infrastructure.resourcing.controller;

//import com.example.client.JobClient;
//import com.example.client.JobPipelineClient;
import com.example.commons.client.JobPipelineClient;
import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.infrastructure.resourcing.service.ResourceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

import io.micronaut.http.client.HttpClient;

import static io.micronaut.http.HttpResponse.ok;

@Controller("/")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Validated
public class AllocationController {
    @Inject
    private final ResourceService resourceService;
    @Inject
    private final JobPipelineClient jobPipelineClient;

//    @Inject
////    @Client("http://localhost:9610/")
//    private final HttpClient client;

    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getById(@PathVariable long id) {
        return HttpResponse.ok(id);
    }

    @Post(uri = "/", produces = MediaType.APPLICATION_JSON)
    // TODO: accepted model
    public HttpResponse<?> post(@RequestBody JsonNode obj) throws IOException {
        // TODO: mocking flow
        var example = "example-response.json";
        var input = Thread.currentThread().getContextClassLoader().getResourceAsStream(example);
        var json = new ObjectMapper().readValue(input, JsonNode.class);
        return ok(json);
    }

    @Get(uri="/ping")
//    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> ping() {
        return ok("pong!");
    }

    @Get(uri = "/fake/{jobId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<String> fake(@PathVariable long jobId) {
        jobPipelineClient.updateStatus(new ExecutionStepPipelineModel(jobId, "Infrastructure", "Start allocation", "Starting resources allocation"));
        jobPipelineClient.updateStatus(new ExecutionStepPipelineModel(jobId, "Infrastructure", "End allocation", "Resources allocation complete"));
        return ok("pong!");
    }
}
