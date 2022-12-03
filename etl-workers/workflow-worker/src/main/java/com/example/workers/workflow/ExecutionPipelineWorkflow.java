package com.example.workers.workflow;

import com.example.commons.model.ExecutionStepPipelineModel;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.util.List;

@WorkflowInterface
public interface ExecutionPipelineWorkflow {
    // INFO: Pipeline workflow execution contains all info about
    // TODO: who (driver IP&Port) & what is executing the pipeline

    @WorkflowMethod
    void open(int id);

    @SignalMethod
    void start();

    @SignalMethod
    void close();

    // Class that has all information
//    void start(Pipeline pipeline) throws JsonProcessingException; // Class that has all information

    @SignalMethod
    void end();

    @SignalMethod
    void terminate();

    @SignalMethod
    void update(ExecutionStepPipelineModel executionStep);

    @QueryMethod
    List<ExecutionStepPipelineModel> getSteps();
}
