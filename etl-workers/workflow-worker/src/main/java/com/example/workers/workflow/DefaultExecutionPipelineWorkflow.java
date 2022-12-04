package com.example.workers.workflow;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.workers.activities.ExecutionActivities;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DefaultExecutionPipelineWorkflow implements ExecutionPipelineWorkflow {

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(20))
            .build();
    private final ExecutionActivities activities = Workflow.newActivityStub(ExecutionActivities.class, options);

    private List<ExecutionStepPipelineModel> steps = new ArrayList<>();
    private boolean exit;
    private long id;

    @Override
    public void open(long id) {
        this.id = id;
        Workflow.await(() -> exit);
    }

    @Override
    public void start() {
        System.out.println("start() | end");
        activities.start(id);
    }

    @Override
    public void close() {

    }

    @Override
    public void end() {
        System.out.println();
        this.exit = true;
    }

    @Override
    public void terminate() {

    }

    @Override
    public void update(ExecutionStepPipelineModel executionStep) {
        System.out.println("update(ExecutionStep executionStep)");
        steps.add(executionStep);
    }

    @Override
    public List<ExecutionStepPipelineModel> getSteps() {
        System.out.println("getSteps()");
        return steps;
    }
}
