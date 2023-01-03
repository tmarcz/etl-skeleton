package com.example.scheduler.service;

import com.example.scheduler.model.JobPipelineSchedulerModel;
import com.example.scheduler.model.PipelineSchedulerModel;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Optional<JobPipelineSchedulerModel> getById(Long id);
    List<JobPipelineSchedulerModel> getAllBySchedulerId(Long id);
    List<JobPipelineSchedulerModel> getAll();
}
