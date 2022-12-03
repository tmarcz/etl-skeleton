package com.example.controller;

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

@Controller("/job")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Validated
public class JobPipelineController {
    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getById(@PathVariable long id) {
        return HttpResponse.ok(id);
    }

    @Post(uri = "/", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> post(@RequestBody JsonNode obj) throws IOException {
        return HttpResponse.ok();
    }

    @Get(uri="/ping")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> ping() {
        return ok("pong!");
    }
}
