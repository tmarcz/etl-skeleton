package com.example.scheduler.service;

import com.example.scheduler.model.PipelineSchedulerModel;

import java.util.List;
import java.util.Optional;

public interface PipelineSchedulerService {
    PipelineSchedulerModel create(PipelineSchedulerModel model);
    PipelineSchedulerModel createCron(PipelineSchedulerModel model);
    PipelineSchedulerModel createInstant(PipelineSchedulerModel model);
    PipelineSchedulerModel createCalendar(PipelineSchedulerModel model);

    PipelineSchedulerModel update(PipelineSchedulerModel model);
    PipelineSchedulerModel updateCron(PipelineSchedulerModel model);
    PipelineSchedulerModel updateCalendar(PipelineSchedulerModel model);

    Optional<PipelineSchedulerModel> getById(Long id);

    List<PipelineSchedulerModel> getAll();

    PipelineSchedulerModel delete(Long id);

    Optional<PipelineSchedulerModel> runFirst();

    List<PipelineSchedulerModel> runAll();

    void runAllAsync(int threadCount);

    void complete(int jobId, boolean success);

    PipelineSchedulerModel instantRun(long pipelineId);
}
