package com.example.workers.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class WorkflowWorker {
    public static final String TASK_QUEUE = "execution";

    @SuppressWarnings("CatchAndPrintStackTrace")
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(DefaultExecutionPipelineWorkflow.class);

        factory.start();
        System.out.println("Worker started for task queue: " + TASK_QUEUE);
    }
}
