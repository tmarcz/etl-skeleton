package com.example.etl.job.controller;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.etl.job.service.JobPipelineService;
import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static io.micronaut.http.HttpResponse.ok;

@Controller("/")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Validated
public class JobPipelineController {

    @Inject
    private JobPipelineService jobPipelineService;

    @Get(uri = "/start/{pipelineId}/{schedulerId}/{jobId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> start(@PathVariable long pipelineId, @PathVariable long schedulerId,@PathVariable long jobId) {
        var startedId = jobPipelineService.start(pipelineId, schedulerId, jobId);
        return HttpResponse.ok(startedId);
    }

    @Get(uri = "/stop/{jobId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> stop(@PathVariable long jobId) {
        var stoppedId = jobPipelineService.stop(jobId);
        return HttpResponse.ok(stoppedId);
    }

    @Post(uri = "/status", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateStatus(@Body ExecutionStepPipelineModel executionStepPipelineModel) {
        var response = jobPipelineService.updateStatus(executionStepPipelineModel);
        return HttpResponse.ok(response);
    }

    @Get(uri = "/{jobId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getStatus(@PathVariable long jobId) {
        var statuses = jobPipelineService.getStatus(jobId);
        return HttpResponse.ok(statuses);
    }

    @Get(uri = "/ping")
//    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> ping() {
        return ok("pong!");
    }
}
