package com.example.commons.mock;

import com.example.commons.client.JobPipelineClient;
import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import io.micronaut.http.HttpResponse;

import static io.micronaut.http.HttpResponse.ok;

public class JobPipelineClientMock implements JobPipelineClient {
    @Override
    public HttpResponse<?> sendStatus(long id) {
        System.out.println("[mock] JobPipelineClient.sendStatus(" + id + ")");
        return ok("");
    }

    @Override
    public HttpResponse<String> ping() {
        System.out.println("[mock] JobPipelineClient.ping()");
        return ok("");
    }

    @Override
    public HttpResponse<ResponseExecutionStepPipelineModel> updateStatus(ExecutionStepPipelineModel model) {
        System.out.println("[mock] JobPipelineClient.updateStatus(" + model.toString() + ")");
        return ok(new ResponseExecutionStepPipelineModel(true));
    }
}
