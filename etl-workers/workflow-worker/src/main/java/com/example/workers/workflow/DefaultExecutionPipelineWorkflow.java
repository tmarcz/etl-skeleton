package com.example.workers.workflow;

import com.example.activities.ExecutionActivities;
import com.example.dto.ExecutionStep;
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

    private List<ExecutionStep> steps = new ArrayList<>();
    private boolean exit;
    private int id;

//    @Override
//    public void open() {
//        Workflow.await(() -> exit);
//    }

//    @Override
//    public void start(int id) {
////        System.out.println("start(int id) | start");
////
////        Promise<String> start = Async.function(activities::start, id);
////
////        System.out.println("start(int id) | end");
////
////        System.out.println("start(int id): " + start.get());
////
////        this.end();
//
////        try {
////            Thread.sleep(10000);
////        } catch (Exception ignored) {
////        }
//
//        Workflow.await(() -> exit);
//    }

//    @Override
//    public void start(Pipeline pipeline) throws JsonProcessingException {
//        System.out.println("start(Pipeline pipeline)");
//        System.out.println(new ObjectMapper().writeValueAsString(pipeline));
//        Workflow.await(() -> exit);
//    }

    @Override
    public void open(int id) {
        this.id = id;
        Workflow.await(() -> exit);
    }

    @Override
    public void start() {
        System.out.println("start() | end");
        activities.start(id);
//        Promise<String> start = Async.function(activities::start, this.id);
//        System.out.println("start() | start | " + start.get());
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
    public void update(ExecutionStep executionStep) {
        System.out.println("update(ExecutionStep executionStep)");
        steps.add(executionStep);
    }

    @Override
    public List<ExecutionStep> getSteps() {
        System.out.println("getSteps()");
        return steps;
    }
}
