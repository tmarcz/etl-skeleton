package com.example.workers.workflow;

import com.example.commons.client.DefaultEngineClient;
import com.example.commons.client.DefaultJobPipelineClient;
import com.example.commons.model.ApplicationSuiteModel;
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
    private long pipeline = 0;
    private ApplicationSuiteModel applicationSuite;
    private String resources = null;
    private String driver = null;

    @Override
    public void open(long id) {
        this.id = id;
        Workflow.await(() -> exit);
    }

    @Override
    public void start() {
        System.out.println("######### start ################## start ################## start ################## start #########");

        steps.add(getStatus("Start", "Open activities"));

        if(pipeline == 0) {
            update(getStatus("Pipeline", "Checking pipeline definition"));
            pipeline = activities.pipelineStep(id);
        }

        if(applicationSuite == null) {
//        if(pipeline > 0 && applicationSuite == null) {
            update(getStatus("Engine", "Checking pipeline implementation & resourcing"));
            applicationSuite = activities.engineStep(pipeline);
        }

        // TODO: extract from application suite resources for resource infra service
        if(resources == null) {
//        if(applicationSuite != null &&  resources == null) {
            update(getStatus("Infrastructure", "Allocating infrastructure resources"));
            resources = activities.resourceStep(pipeline);
        }

        // TODO: driver application result model
        if(driver == null){
            applicationSuite.setJobId(id); // TODO: mock
            driver = activities.driverStep(applicationSuite);
        }

        // closing
        exit = true;

        System.out.println("######### end ################## end ################## end ################## end #########");
    }


    @Override
    public void close() {

    }

    @Override
    public void end() {
        steps.add(getStatus("End", "Close activities"));
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

    private ExecutionStepPipelineModel getStatus(String title, String message){
        return new ExecutionStepPipelineModel(1L,"Workflow", title, message);
    }
}
