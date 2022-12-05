package com.example.commons.client;

import com.example.commons.model.ApplicationSuiteModel;
import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DefaultDriverClient {

    public static String ping() {
        try {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:9660/ping"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = response.body();
        return result;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String run(ApplicationSuiteModel model) {
        try {
            var objectMapper = new ObjectMapper();
            var body = objectMapper.writeValueAsString(model);

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                    URI.create("http://localhost:9660/run"))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var result = response.body();
            return result;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<ExecutionStepPipelineModel> getStatus(long jobId) {
        try {
            return null;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
