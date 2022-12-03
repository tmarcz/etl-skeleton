package com.example.scheduler.controller;

import com.example.scheduler.service.SchedulerService;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

@Controller("/scheduler")
public class SchedulerController {

    @Inject
    private SchedulerService schedulerService;

    @Post("/{name}")
    public String create(String name) {
        return "create";
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

    @Put("/deactivate/{id}")
    public String deactivate(int id) {
        // TODO: deactivate, stop if running job, inform workflow and clean next interval date
        return "";
    }

    @Put("/stop/{id}")
    public String stop(int id) {
        // TODO: stop running job, inform workflow and set next interval date
        return "";
    }

    @Put("/complete/{id}")
    public String complete(int id) {
        // TODO: complete running job and set next interval date
        return "";
    }

    @Post("/force/")
    public String force() throws InterruptedException {
        int cores = 3; // Runtime.getRuntime().availableProcessors();
        schedulerService.runAllAsync(cores);
        return "force";
    }

    @Get("/workflow/")
    public String workflow(int id) {
        // TODO: test communication to workflow service

        return "get";
    }

    // TODO:
    // 1. service & repo for "jobs"
    // 2. controller: insert/update -> next run date
    // 3. controller: close job -> next run date | complete

}

