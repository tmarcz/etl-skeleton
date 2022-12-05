package com.example.commons.client;

import com.example.commons.model.ApplicationSuiteModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DefaultInfrastructureClient {

//    public static ApplicationSuiteModel get(JsonNode pipelineId) {
//        try {
//            var client = HttpClient.newHttpClient();
//            var request = HttpRequest.newBuilder(
//                    URI.create("http://localhost:9620/status/" + pipelineId))
//                    .header("accept", "application/json")
//                    .header("content-type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(body))
//                    .build();
//
//            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            var objectMapper = new ObjectMapper();
//            var result = objectMapper.readValue(response.body(), ApplicationSuiteModel.class);
//            return result;
//        } catch (Exception exception) {
//            throw new RuntimeException(exception);
//        }
//    }

    public static String ping() {
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                    URI.create("http://localhost:9620/ping"))
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

    public static String fake(long jobId) {
        try {
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                    URI.create("http://localhost:9620/fake/" + jobId))
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
}
