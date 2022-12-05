package com.example.workers.activities.client;

import com.example.commons.model.ExecutionStepPipelineModel;
import com.example.commons.model.ResponseExecutionStepPipelineModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultJobPipelineClient {

    public static String ping() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(
                URI.create("http://localhost:9609/job/ping"))
                .header("accept", "text/plain")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var result = response.body();
        return result;
    }

    public static ResponseExecutionStepPipelineModel updateStatus(ExecutionStepPipelineModel model) {

        try {
//        ExecutionStepPipelineModel model = new ExecutionStepPipelineModel(1, "Worker", "", "");

            var mapper = new ObjectMapper();
            var body = mapper.writeValueAsString(model);

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create("https://httpbin.org/post"))
//                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                    .build();

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                    URI.create("http://localhost:9609/status"))
                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var result = response.body();

            return null;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}

/*

    @Post(uri = "/status", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateStatus(@Body ExecutionStepPipelineModel executionStepPipelineModel) {
        var response = jobPipelineService.updateStatus(executionStepPipelineModel);
        return HttpResponse.ok(response);
    }

 */