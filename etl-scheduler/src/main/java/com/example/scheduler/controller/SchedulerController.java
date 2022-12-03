package com.example.scheduler.controller;

import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.service.PipelineSchedulerService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.validation.Valid;

import static io.micronaut.http.HttpResponse.ok;

@Controller("/")
public class SchedulerController {
    @Inject
    private PipelineSchedulerService pipelineSchedulerService;

    @Get("/instant/{pipelineId}")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> instant(@PathVariable long pipelineId) {
        return ok("ok!");
    }

    @Post("/")
    public HttpResponse<?>  create(@Body @Valid PipelineSchedulerModel model) {
        return ok("todo!");
    }

    @Get("/{id}")
    public String get(int id) {
        return "get";
    }

    @Get("/")
    public String getAll() {
        return "getAll";
    }

    @Put("/{name}")
    public String update(String name) {
        return "update";
    }

    @Delete("/{id}")
    public String delete(int id) {
        // TODO: set deleted flag, stop if running job, inform workflow and clean next interval date
        return "delete";
    }

    @Get("/deactivate/{id}")
    public String deactivate(int id) {
        // TODO: deactivate, stop if running job, inform workflow and clean next interval date
        return "";
    }

    @Get("/stop/{id}")
    public String stop(int id) {
        // TODO: stop running job, inform workflow and set next interval date
        return "";
    }

    @Get("/complete/{id}")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String>  complete(int id) {
        // TODO: complete running job and set next interval date
        return ok("ok!");
    }

    @Post("/force/")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> force() throws InterruptedException {
        int cores = 3; // Runtime.getRuntime().availableProcessors();
        pipelineSchedulerService.runAllAsync(cores);
        return ok("ok!");
    }

    @Get("/workflow/")
    public String workflow(int id) {
        // TODO: test communication to workflow service

        return "get";
    }

    @Get(uri="/ping")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> ping() throws Exception {
        return ok("pong!");
    }

}

