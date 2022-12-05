package com.example.engine.pipeline.controller;

import com.example.commons.client.JobPipelineClient;
import com.example.commons.mock.JobPipelineClientMock;
import com.example.commons.model.ApplicationSuiteModel;
import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.engine.pipeline.service.ImplementationStepPipelineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;

import java.io.IOException;

import static io.micronaut.http.HttpResponse.ok;

@Controller("/")
@Validated
public class EngineController {
    @Inject
    private ImplementationStepPipelineService implementationStepPipelineService;
    @Inject
    private JobPipelineClient jobPipelineClient;
    // TODO: quick test only
//    private JobPipelineClient jobPipelineClient = new JobPipelineClientMock();

    @Get(uri = "/{pipelineId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<ApplicationSuiteModel> get(@PathVariable long pipelineId) throws IOException {
        // TODO: JobPipelineClient & statuses updates -> moving to something like log4j and use across the whole module
        jobPipelineClient.updateStatus(new ExecutionStepPipelineModel(pipelineId, "Engine", "Start", "Preparing pipeline's implementation with resources"));
        var example = "example-response.json";
        var input = Thread.currentThread().getContextClassLoader().getResourceAsStream(example);
        var json = new ObjectMapper().readValue(input, ApplicationSuiteModel.class);
        jobPipelineClient.updateStatus(new ExecutionStepPipelineModel(pipelineId, "Engine", "End", "Returning pipeline's implementation with resources"));
        return HttpResponse.ok(json);
    }

    @Get(uri="/ping")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> ping() throws Exception {
        return ok("pong!");
    }
}
