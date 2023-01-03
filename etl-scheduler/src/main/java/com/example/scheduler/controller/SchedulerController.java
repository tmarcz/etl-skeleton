package com.example.scheduler.controller;

import com.example.scheduler.model.PipelineSchedulerModel;
import com.example.scheduler.service.PipelineSchedulerService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.validation.Valid;

import java.util.List;

import static io.micronaut.http.HttpResponse.ok;

@Controller("/")
public class SchedulerController {
    @Inject
    private PipelineSchedulerService pipelineSchedulerService;
    @Inject
    private ApplicationContext applicationContext;

    @Get("/{id}")
    public HttpResponse<?> get(@PathVariable long id) {
        var result = pipelineSchedulerService.getById(id);
        return result.map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    @Get("/getByPipelineId/{id}")
    public HttpResponse<?> getByPipelineId(@PathVariable long id) {
        return ok("TODO: getByPipelineId");
    }

    @Post("/")
    public HttpResponse<PipelineSchedulerModel>  create(@Body @Valid PipelineSchedulerModel model) {
       var result = pipelineSchedulerService.create(model);
       return ok(result);
    }

    @Put("/")
    public HttpResponse<PipelineSchedulerModel>  update(@Body @Valid PipelineSchedulerModel model) {
        var result = pipelineSchedulerService.update(model);
        return ok(result);
    }

    @Delete("/{id}")
    public HttpResponse<PipelineSchedulerModel> delete(@PathVariable long id) {
        // TODO: set deleted flag, stop if running job or inform?, inform workflow and clean next interval date?
        var result = pipelineSchedulerService.delete(id);
        return ok(result);
    }

    // TODO: control plane activities only for "/force/*" ?
    @Get("/")
    public HttpResponse<List<PipelineSchedulerModel>>  getAll() {
        var result = pipelineSchedulerService.getAll();
        return ok(result);
    }

    @Get("/force/run/{pipelineId}")
    public HttpResponse<PipelineSchedulerModel> forceRun(@PathVariable long pipelineId) {
        var result = pipelineSchedulerService.instantRun(pipelineId);
        return ok(result);
    }

    @Get("/force/runFirst")
    public HttpResponse<PipelineSchedulerModel> forceRunFirst() {
        var result = pipelineSchedulerService.runFirst();
        return result.map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound);
    }

    @Get("/force/runAll")
    public HttpResponse<List<PipelineSchedulerModel>> forceRunAll() {
        var result = pipelineSchedulerService.runAll();
        return ok(result);
    }

    @Get("/force/runAllAsync/{cores}")
    public HttpResponse<List<PipelineSchedulerModel>> forceRunAllAsync(@PathVariable int cores) {
        cores = cores == 0 ? Runtime.getRuntime().availableProcessors() : cores;
        var result = pipelineSchedulerService.runAllAsync(cores);
        return ok(result);
    }



    @Get("/deactivate/{id}")
    public String deactivate(int id) {
        // TODO: deactivate, stop if running job, inform workflow and clean next interval date
        return "";
    }

    // TODO: stop job
//    @Get("/stop/{id}")
//    public String stop(int id) {
//        // TODO: stop running job, inform workflow and set next interval date
//        return "";
//    }

    @Get("/complete/{id}")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String>  complete(int id) {
        // TODO: complete running job and set next interval date
        return ok("ok!");
    }

    @Post("/force/")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> force() throws InterruptedException {
        int cores = 1; // Runtime.getRuntime().availableProcessors();
        pipelineSchedulerService.runAllAsync(cores);
        return ok("ok!");
    }

    @Get(uri="/ping")
    @Produces(value = MediaType.TEXT_PLAIN)
    public HttpResponse<String> ping() throws Exception {
        return ok("pong!");
    }





    // TODO: to delete: ?
    @Get("/instant/{pipelineId}")
    public HttpResponse<?> instant(@PathVariable long pipelineId) {
//        var result = pipelineSchedulerService.createInstant(pipelineId);
        return ok("ok!");
    }
}

