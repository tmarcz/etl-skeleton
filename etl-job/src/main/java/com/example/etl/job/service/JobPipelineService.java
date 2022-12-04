package com.example.etl.job.service;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import com.example.etl.job.model.JobPipelineModel;

import java.util.List;
import java.util.Optional;

public interface JobPipelineService {
    long start(long pipelineId, long schedulerId, long jobId);

    long stop(long jobId);

    ResponseExecutionStepPipelineModel updateStatus(ExecutionStepPipelineModel executionStepPipelineModel);

    List<ExecutionStepPipelineModel> getStatus(long jobId);
}
