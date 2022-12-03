package com.example.scheduler.client;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;

import java.net.http.HttpClient;

@Primary
@Singleton
public class DefaultWorkflowService implements WorkflowService{
    @Override
    public boolean startJob(int id) {
        var client = HttpClient.newHttpClient();
        return true;
    }

    @Override
    public boolean stopJob(int id) {
        var client = HttpClient.newHttpClient();
        return true;
    }
}
