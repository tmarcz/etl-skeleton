package com.example.etl.job.service;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import com.example.etl.job.mapping.JobPipelineMapper;
import com.example.etl.job.model.JobPipelineModel;
import com.example.etl.job.repository.JobPipelineRepository;
import com.example.workers.workflow.ExecutionPipelineWorkflow;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import java.util.List;
import java.util.Optional;

@Primary
@Singleton
public class DefaultJobPipelineService implements JobPipelineService {
    @Inject
    private JobPipelineRepository repository;
    private final JobPipelineMapper mapper = JobPipelineMapper.MAPPER;

    public static final String TASK_QUEUE = "execution";
    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    private static final WorkflowClient client = WorkflowClient.newInstance(service);
    private static final WorkerFactory factory = WorkerFactory.newInstance(client);

    @Override
    public long start(long pipelineId, long schedulerId, long jobId) {

        System.out.println("start(int id) | start");

        WorkflowOptions options =
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId(String.valueOf(jobId))
                        .build();

        ExecutionPipelineWorkflow workflow = client.newWorkflowStub(ExecutionPipelineWorkflow.class, options);
        try {
            WorkflowExecution execution = WorkflowClient.start(workflow::open, jobId);
            System.out.println("Workflow started: " + execution);
            workflow.start();
        } catch (WorkflowExecutionAlreadyStarted e) {
            System.out.println("Workflow already running: " + e.getExecution());
        }

        return jobId;
    }

    @Override
    public long stop(long jobId) {
        WorkflowOptions options =
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId(String.valueOf(jobId))
                        .build();

        ExecutionPipelineWorkflow workflow = client.newWorkflowStub(ExecutionPipelineWorkflow.class, options);
        workflow.close();
        return jobId;
    }

    @Override
    public ResponseExecutionStepPipelineModel updateStatus(ExecutionStepPipelineModel executionStepPipelineModel) {
        var id = executionStepPipelineModel.getPipelineId();
        WorkflowOptions options =
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId(String.valueOf(id))
                        .build();

        ExecutionPipelineWorkflow workflow = client.newWorkflowStub(ExecutionPipelineWorkflow.class, options);
        workflow.update(executionStepPipelineModel);

        // TODO: potential option to stop the flow
        var result = new ResponseExecutionStepPipelineModel(true);
        return result;
    }

    @Override
    public List<ExecutionStepPipelineModel> getStatus(long jobId) {
        WorkflowOptions options =
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE)
                        .setWorkflowId(String.valueOf(jobId))
                        .build();

        ExecutionPipelineWorkflow workflow = client.newWorkflowStub(ExecutionPipelineWorkflow.class, options);
        var result = workflow.getSteps();
        return result;
    }

}