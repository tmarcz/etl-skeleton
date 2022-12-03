package com.example.scheduler.service;

import com.example.scheduler.model.PipelineSchedulerModel;

import java.util.Optional;

public interface PipelineSchedulerService {
    PipelineSchedulerModel create(PipelineSchedulerModel model);
    Optional<PipelineSchedulerModel> getById(Long id);

    boolean runFirst();
    void runAll();
    void runAllAsync(int threadCount) throws InterruptedException;
}
