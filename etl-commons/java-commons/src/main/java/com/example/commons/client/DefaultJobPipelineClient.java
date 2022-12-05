package com.example.commons.client;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dattri.jsonbodyhandler.JsonBodyHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DefaultJobPipelineClient {

    public static String ping() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:9609/ping"))
                .header("accept", "text/plain")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = response.body();
        return result;
    }

    public static ResponseExecutionStepPipelineModel updateStatus(ExecutionStepPipelineModel model) {
        try {
            var objectMapper = new ObjectMapper();
            var body = objectMapper.writeValueAsString(model);

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                    URI.create("http://localhost:9609/status"))
//                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var result = objectMapper.readValue(response.body(), ResponseExecutionStepPipelineModel.class);
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
