package com.example.scheduler.controller;

import com.example.scheduler.model.JobPipelineSchedulerModel;
import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.service.JobService;
import com.example.scheduler.service.PipelineSchedulerService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;

import java.util.List;

import static io.micronaut.http.HttpResponse.ok;

@Controller("/job")
public class JobRunController {
    @Inject
    private JobService jobService;
    @Inject
    private ApplicationContext applicationContext;

    @Get("/{id}")
    public HttpResponse<?> get(@PathVariable long id) {
        var result = jobService.getById(id);
        return result.map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    @Get("/getAll")
    public HttpResponse<List<JobPipelineSchedulerModel>> getAll() {
        var result = jobService.getAll();
        return ok(result);
    }

    @Get("/getAllBySchedulerId/{id}")
    public HttpResponse<List<JobPipelineSchedulerModel>> getAllBySchedulerId(@PathVariable long id) {
        var result = jobService.getAllBySchedulerId(id);
        return ok(result);
    }
}
